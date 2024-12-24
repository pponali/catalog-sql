package com.scaler.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class FeatureValueEvent {
    
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED,
        VALIDATE,
        MONITOR,
        VALUE_CHANGE,
        STATUS_CHANGE,
        VALIDATION,
        TRANSFORMATION
    }
    
    private Long featureId;
    private Long productId;
    private String oldValue;
    private String newValue;
    private EventType eventType;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
}
