package com.scaler.event;

import com.scaler.entity.ProductFeatureValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureValueEvent {
    private Long featureId;
    private Long productId;
    private com.scaler.model.FeatureValueEvent.EventType eventType;
    private LocalDateTime eventTime = LocalDateTime.now();
    private String oldValue;
    private String newValue;
    private String validationMessage;
    private String value;
    private String status;


    public static FeatureValueEvent createMonitoringEvent(ProductFeatureValue value) {
        return FeatureValueEvent.builder()
                .featureId(value.getFeature().getId())
                .value(value.getValueAsString())
                .status(value.getValidationStatus())
                .eventType(com.scaler.model.FeatureValueEvent.EventType.MONITOR)
                .build();
    }

    public static FeatureValueEvent createValidationEvent(ProductFeatureValue value, boolean isValid, String message) {
        return FeatureValueEvent.builder()
                .featureId(value.getFeature().getId())
                .value(value.getValueAsString())
                .status(value.getValidationStatus())
                .validationMessage(message)
                .eventType(com.scaler.model.FeatureValueEvent.EventType.VALIDATION)
                .build();
    }

    public static FeatureValueEvent createValueChangeEvent(ProductFeatureValue value, String oldValue, String newValue) {
        return FeatureValueEvent.builder()
                .featureId(value.getFeature().getId())
                .value(value.getValueAsString())
                .oldValue(oldValue)
                .newValue(newValue)
                .status(value.getValidationStatus())
                .eventType(com.scaler.model.FeatureValueEvent.EventType.VALUE_CHANGE)
                .build();
    }

    public static FeatureValueEvent createStatusChangeEvent(ProductFeatureValue value) {
        return FeatureValueEvent.builder()
                .featureId(value.getFeature().getId())
                .value(value.getValueAsString())
                .status(value.getValidationStatus())
                .eventType(com.scaler.model.FeatureValueEvent.EventType.STATUS_CHANGE)
                .build();
    }
}
