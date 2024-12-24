package com.scaler.service.monitoring;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.event.FeatureValueEvent;
import com.scaler.model.MonitoringMetrics;
import com.scaler.repository.ProductFeatureValueRepository;
import com.scaler.service.cache.FeatureValueCacheService;
import com.scaler.service.event.FeatureValueEventService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeatureValueMonitoringService {

    private final MeterRegistry meterRegistry;
    private final FeatureValueCacheService cacheService;
    private final FeatureValueEventService eventService;
    private final ProductFeatureValueRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    private final Map<String, Timer> operationTimers = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> errorCounters = new ConcurrentHashMap<>();
    private final Map<Long, LocalDateTime> lastUpdateTimes = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // Initialize basic metrics
        meterRegistry.gauge("feature.value.updates", new AtomicLong(0));
        meterRegistry.gauge("feature.value.errors", new AtomicLong(0));
    }

    public void recordOperation(String operation, Duration duration) {
        Timer timer = operationTimers.computeIfAbsent(operation,
                op -> Timer.builder("feature.value.operation")
                        .tag("operation", op)
                        .register(meterRegistry));
        timer.record(duration);
    }

    public void recordError(String operation) {
        errorCounters.computeIfAbsent(operation, op -> new AtomicLong())
                .incrementAndGet();
        meterRegistry.counter("feature.value.errors", "operation", operation)
                .increment();
    }

    public void recordUpdate(Long featureId, ProductFeatureValueDTO value) {
        lastUpdateTimes.put(featureId, LocalDateTime.now());
        meterRegistry.counter("feature.value.updates", "featureId", featureId.toString())
                .increment();
    }

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void checkStaleValues() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        lastUpdateTimes.forEach((featureId, lastUpdate) -> {
            if (lastUpdate.isBefore(threshold)) {
                log.warn("Feature {} has not been updated in 24 hours", featureId);
                meterRegistry.counter("feature.value.stale", "featureId", featureId.toString())
                        .increment();
            }
        });
    }

    public MonitoringMetrics getMetrics() {
        Map<String, Double> avgOperationTimes = new ConcurrentHashMap<>();
        operationTimers.forEach((operation, timer) -> 
            avgOperationTimes.put(operation, timer.mean(java.util.concurrent.TimeUnit.MILLISECONDS))
        );

        Map<String, Long> errors = new ConcurrentHashMap<>();
        errorCounters.forEach((operation, counter) -> 
            errors.put(operation, counter.get())
        );

        return MonitoringMetrics.builder()
                .operationTimes(avgOperationTimes)
                .errorCounts(errors)
                .cacheStats(cacheService.getAllCacheStats())
                .eventHistory(eventService.getEventHistory(null, com.scaler.model.FeatureValueEvent.EventType.VALUE_CHANGE))
                .lastUpdateTimes(new ConcurrentHashMap<>(lastUpdateTimes))
                .build();
    }

    public void resetMetrics() {
        operationTimers.clear();
        errorCounters.clear();
        meterRegistry.clear();
        init();
    }

    public Map<String, Double> getOperationTimings() {
        Map<String, Double> timings = new ConcurrentHashMap<>();
        operationTimers.forEach((operation, timer) -> 
            timings.put(operation, timer.mean(java.util.concurrent.TimeUnit.MILLISECONDS))
        );
        return timings;
    }

    public Map<String, Long> getErrorCounts() {
        Map<String, Long> counts = new ConcurrentHashMap<>();
        errorCounters.forEach((operation, counter) -> 
            counts.put(operation, counter.get())
        );
        return counts;
    }

    public double getAverageOperationTime(String operationName) {
        Timer timer = operationTimers.get(operationName);
        return timer != null ? timer.mean(java.util.concurrent.TimeUnit.MILLISECONDS) : 0.0;
    }

    public Map<String, Double> getAllOperationTimes() {
        return operationTimers.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().mean(java.util.concurrent.TimeUnit.MILLISECONDS)
            ));
    }

    public void monitorFeatureValue(ProductFeatureValue value) {
        monitorFeatureValueChange(null, value);
    }

    public void validateFeatureValue(ProductFeatureValue featureValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .featureId(featureValue.getFeature().getId())
                .productId(featureValue.getProduct().getId())
                .oldValue(featureValue.getStringValue())
                .newValue(featureValue.getStringValue())
                .eventType(com.scaler.model.FeatureValueEvent.EventType.VALUE_CHANGE)
                .build();
        eventPublisher.publishEvent(event);
    }

    public void updateFeatureValue(ProductFeatureValue featureValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .featureId(featureValue.getFeature().getId())
                .productId(featureValue.getProduct().getId())
                .oldValue(featureValue.getStringValue())
                .newValue(featureValue.getStringValue())
                .eventType(com.scaler.model.FeatureValueEvent.EventType.VALUE_CHANGE)
                .build();
        eventPublisher.publishEvent(event);
    }

    public void monitorFeatureValueChange(ProductFeatureValue oldValue, ProductFeatureValue newValue) {
        if (oldValue == null && newValue != null) {
            // Feature value created
            createEvent(newValue, com.scaler.model.FeatureValueEvent.EventType.CREATED);
        } else if (oldValue != null && newValue == null) {
            // Feature value deleted
            createEvent(oldValue, com.scaler.model.FeatureValueEvent.EventType.DELETED);
        } else if (!oldValue.equals(newValue)) {
            // Feature value updated
            createEvent(newValue, com.scaler.model.FeatureValueEvent.EventType.UPDATED);
        }
    }

    private void createEvent(ProductFeatureValue value, com.scaler.model.FeatureValueEvent.EventType eventType) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .featureId(value.getFeature().getId())
                .productId(value.getProduct().getId())
                .oldValue(eventType == com.scaler.model.FeatureValueEvent.EventType.UPDATED ? value.getStringValue() : null)
                .newValue(value.getStringValue())
                .eventType(eventType)
                .build();
        
        eventPublisher.publishEvent(event);
    }
}
