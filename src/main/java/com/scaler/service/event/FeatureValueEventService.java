package com.scaler.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeatureValueEventService {
    private final FeatureValueEventRepository repository;
    private final ObjectMapper objectMapper;
    private final Map<String, List<FeatureValueEvent>> eventHistory = new ConcurrentHashMap<>();

    public void publishValueChangeEvent(ProductFeatureValueDTO oldValue, ProductFeatureValueDTO newValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .eventType(FeatureValueEvent.EventType.VALUE_CHANGE)
                .featureId(newValue.getFeatureMapping().getFeature().getId())
                .oldValue(oldValue != null ? oldValue.getAttributeValues() : null)
                .newValue(newValue.getAttributeValues())
                .timestamp(LocalDateTime.now())
                .build();
        saveEvent(event);
    }

    public void publishValidationEvent(ProductFeatureValueDTO value, List<String> violations) {
        try {
            String metadataJson = objectMapper.writeValueAsString(Map.of("violations", violations));
            FeatureValueEvent event = FeatureValueEvent.builder()
                    .eventType(FeatureValueEvent.EventType.VALIDATION)
                    .featureId(value.getFeatureMapping().getFeature().getId())
                    .newValue(value.getAttributeValues())
                    .metadata(metadataJson)
                    .timestamp(LocalDateTime.now())
                    .build();
            saveEvent(event);
        } catch (JsonProcessingException e) {
            log.error("Error serializing metadata to JSON", e);
        }
    }

    public void publishTransformationEvent(ProductFeatureValueDTO originalValue, ProductFeatureValueDTO transformedValue) {
        FeatureValueEvent event = FeatureValueEvent.builder()
                .eventType(FeatureValueEvent.EventType.TRANSFORMATION)
                .featureId(originalValue.getFeatureMapping().getFeature().getId())
                .oldValue(originalValue.getAttributeValues())
                .newValue(transformedValue.getAttributeValues())
                .timestamp(LocalDateTime.now())
                .build();
        saveEvent(event);
    }

    private void saveEvent(FeatureValueEvent event) {
        repository.save(event);
        String key = event.getFeatureId().toString();
        eventHistory.computeIfAbsent(key, k -> new ArrayList<>()).add(event);
        log.debug("Event saved: {}", event);
    }

    public List<FeatureValueEvent> getEventsByFeatureId(UUID featureId) {
        return repository.findByFeatureId(featureId);
    }

    public List<FeatureValueEvent> getEventsByType(com.scaler.model.FeatureValueEvent.EventType eventType) {
        return repository.findByEventType(eventType);
    }

    public List<FeatureValueEvent> getEventHistory(UUID featureId) {
        if (featureId == null) {
            return new ArrayList<>();
        }
        return eventHistory.getOrDefault(featureId.toString(), new ArrayList<>());
    }
}
