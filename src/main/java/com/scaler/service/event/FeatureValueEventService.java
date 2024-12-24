package com.scaler.service.event;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.model.FeatureValueEvent;
import com.scaler.repository.FeatureValueEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeatureValueEventService {
    private final FeatureValueEventRepository repository;
    private final Map<String, List<FeatureValueEvent>> eventHistory = new ConcurrentHashMap<>();

    public void publishValueChangeEvent(ProductFeatureValueDTO oldValue, ProductFeatureValueDTO newValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .eventType(FeatureValueEvent.EventType.VALUE_CHANGE)
                .featureId(Long.parseLong(newValue.getFeatureId().toString()))
                .oldValue(oldValue != null ? oldValue.getValue() : null)
                .newValue(newValue.getValue())
                .timestamp(LocalDateTime.now())
                .build();
        saveEvent(event);
    }

    public void publishValidationEvent(ProductFeatureValueDTO value, List<String> violations) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .eventType(FeatureValueEvent.EventType.VALIDATION)
                .featureId(Long.parseLong(value.getFeatureId().toString()))
                .newValue(value.getValue())
                .metadata(Map.of("violations", violations))
                .timestamp(LocalDateTime.now())
                .build();
        saveEvent(event);
    }

    public void publishTransformationEvent(ProductFeatureValueDTO originalValue, ProductFeatureValueDTO transformedValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .eventType(FeatureValueEvent.EventType.TRANSFORMATION)
                .featureId(Long.parseLong(originalValue.getFeatureId().toString()))
                .oldValue(originalValue.getValue())
                .newValue(transformedValue.getValue())
                .timestamp(LocalDateTime.now())
                .build();
        saveEvent(event);
    }

    private void saveEvent(FeatureValueEvent event) {
        repository.save(event);
        String key = event.getFeatureId() + "_" + event.getEventType();
        eventHistory.computeIfAbsent(key, k -> new ArrayList<>()).add(event);
        log.debug("Event saved: {}", event);
    }

    public List<FeatureValueEvent> getEventsByFeatureId(Long featureId) {
        return repository.findByFeatureId(featureId.toString());
    }

    public List<FeatureValueEvent> getEventsByType(com.scaler.model.FeatureValueEvent.EventType eventType) {
        return repository.findByEventType(eventType);
    }

    public List<FeatureValueEvent> getEventHistory(Long featureId, com.scaler.model.FeatureValueEvent.EventType eventType) {
        String key = featureId.toString() + "_" + eventType;
        return new ArrayList<>(eventHistory.getOrDefault(key, new ArrayList<>()));
    }
}
