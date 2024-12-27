package com.scaler.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feature_value_event")
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long featureId;
    private Long productId;
    private String oldValue;
    private String newValue;
    
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    
    @Column(columnDefinition = "text")
    private String metadata;
    
    private LocalDateTime timestamp;
}
