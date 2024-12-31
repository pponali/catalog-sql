package com.scaler.service.monitoring;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.model.FeatureValueEvent;
import com.scaler.model.MonitoringMetrics;
import com.scaler.repository.ProductFeatureValueRepository;
import com.scaler.service.cache.FeatureValueCacheService;
import com.scaler.service.event.FeatureValueEventService;
import com.scaler.service.ProductFeatureValueService;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeatureValueMonitoringService {

    private final MeterRegistry meterRegistry;
    private final FeatureValueCacheService cacheService;
    private final FeatureValueEventService eventService;
    private final ProductFeatureValueRepository repository;
    private final ProductFeatureValueService featureValueService;
    private final ApplicationEventPublisher eventPublisher;

    private final Map<String, Timer> operationTimers = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> errorCounters = new ConcurrentHashMap<>();
    private final Map<UUID, LocalDateTime> lastUpdateTimes = new ConcurrentHashMap<>();

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

    public void recordUpdate(UUID featureId, ProductFeatureValueDTO value) {
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
        Map<String, Double> avgOperationTimes = operationTimers.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().mean(java.util.concurrent.TimeUnit.MILLISECONDS)
            ));

        Map<String, Long> errors = errorCounters.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get()
            ));

        return MonitoringMetrics.builder()
                .operationTimes(avgOperationTimes)
                .errorCounts(errors)
                .cacheStats(cacheService.getAllCacheStats())
                .eventHistory(eventService.getEventHistory(null))
                .lastUpdateTimes(lastUpdateTimes)
                .build();
    }

    public MonitoringMetrics getMetricsForFeature(UUID featureId) {
        List<ProductFeatureValue> values = repository.findByFeatureId(featureId);
        
        // Calculate metrics
        long totalValues = values.size();
        long validValues = values.stream()
                .filter(v -> "VALID".equals(v.getValidationStatus()))
                .count();
        
        return MonitoringMetrics.builder()
                .featureId(featureId)
                .totalValues(totalValues)
                .validValues(validValues)
                .lastUpdateTime(lastUpdateTimes.get(featureId))
                .build();
    }

    public void resetMetrics() {
        operationTimers.clear();
        errorCounters.clear();
        meterRegistry.clear();
        init();
    }

    public Map<String, Double> getOperationTimings() {
        return operationTimers.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().mean(java.util.concurrent.TimeUnit.MILLISECONDS)
            ));
    }

    public Map<String, Long> getErrorCounts() {
        return errorCounters.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get()
            ));
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
                .eventType(FeatureValueEvent.EventType.VALIDATION)
                .build();
        eventPublisher.publishEvent(event);
    }

    public void updateFeatureValue(ProductFeatureValue featureValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .featureId(featureValue.getFeature().getId())
                .productId(featureValue.getProduct().getId())
                .oldValue(featureValue.getStringValue())
                .newValue(featureValue.getStringValue())
                .eventType(FeatureValueEvent.EventType.VALUE_CHANGE)
                .build();
        eventPublisher.publishEvent(event);
    }

    public void monitorFeatureValueChange(ProductFeatureValue oldValue, ProductFeatureValue newValue) {
        if (oldValue == null && newValue != null) {
            // Feature value created
            createEvent(newValue, FeatureValueEvent.EventType.CREATED);
        } else if (oldValue != null && newValue == null) {
            // Feature value deleted
            createEvent(oldValue, FeatureValueEvent.EventType.DELETED);
        } else if (!oldValue.equals(newValue)) {
            // Feature value updated
            createEvent(newValue, FeatureValueEvent.EventType.UPDATED);
        }
    }

    private void createEvent(ProductFeatureValue value, FeatureValueEvent.EventType eventType) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .featureId(value.getFeature().getId())
                .productId(value.getProduct().getId())
                .oldValue(eventType == FeatureValueEvent.EventType.UPDATED ? value.getStringValue() : null)
                .newValue(value.getStringValue())
                .eventType(eventType)
                .build();
        
        eventPublisher.publishEvent(event);
    }

    private void publishValidationEvent(ProductFeatureValue featureValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .id(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .featureId(featureValue.getFeature().getId())
                .productId(featureValue.getProduct().getId())
                .oldValue(featureValue.getStringValue())
                .newValue(featureValue.getStringValue())
                .eventType(FeatureValueEvent.EventType.VALIDATION)
                .build();
        eventPublisher.publishEvent(event);
    }

    public void monitorFeatureValue(UUID featureId) {
        log.info("Starting monitoring for feature ID: {}", featureId);
        ProductFeatureValueDTO featureValue = featureValueService.findById(featureId)
            .orElseThrow(() -> new IllegalArgumentException("Feature value not found: " + featureId));
        
        // Add monitoring logic here
        // Monitor value changes
        monitorValueChanges(featureId);
        
        // Monitor validation status
        monitorValidationStatus(featureId);
        
        // Monitor performance metrics
        monitorPerformanceMetrics(featureId);
        
        log.info("Monitoring completed for feature ID: {}", featureId);
    }

    public List<FeatureValueEvent> getEventHistory(UUID featureId) {
        return eventService.getEventHistory(featureId);
    }

    private void monitorValueChanges(UUID featureId) {
        // TO DO: implement value change monitoring logic
    }

    private void monitorValidationStatus(UUID featureId) {
        // TO DO: implement validation status monitoring logic
    }

    private void monitorPerformanceMetrics(UUID featureId) {
        // TO DO: implement performance metrics monitoring logic
    }
}
