package com.scaler.model;

import com.scaler.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feature_value_event")
public class FeatureValueEvent extends BaseEntity {
    
    public enum EventType {
        CREATED,
        UPDATED,
        DELETED,
        VALIDATE,
        MONITOR,
        VALUE_CHANGE,
        STATUS_CHANGE,
        VALIDATION,
        TRANSFORMATION,
        ACCESS
    }
    
    private UUID featureId;
    private UUID productId;
    private String oldValue;
    private String newValue;
    
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    @Column(columnDefinition = "text")
    private String metadata;
    
    private LocalDateTime timestamp;


    private String details;

    public static FeatureValueEvent createUpdateEvent(UUID featureId, String oldValue, String newValue) {
        return FeatureValueEvent.builder()
                .id(UUID.randomUUID())
                .featureId(featureId)
                .eventType(com.scaler.model.FeatureValueEvent.EventType.UPDATED)
                .oldValue(oldValue)
                .newValue(newValue)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static FeatureValueEvent createValidationEvent(UUID featureId, String validationMessage) {
        return FeatureValueEvent.builder()
                .id(UUID.randomUUID())
                .featureId(featureId)
                .eventType(com.scaler.model.FeatureValueEvent.EventType.VALIDATION)
                .details(validationMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static FeatureValueEvent createAccessEvent(UUID featureId, String accessType) {
        return FeatureValueEvent.builder()
                .id(UUID.randomUUID())
                .featureId(featureId)
                .eventType(com.scaler.model.FeatureValueEvent.EventType.ACCESS)
                .details(accessType)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
