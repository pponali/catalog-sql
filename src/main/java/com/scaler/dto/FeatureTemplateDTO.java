package com.scaler.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureTemplateDTO {
    private UUID id;
    private String name;
    private String description;
    private String type;
    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String defaultValue;
    private Boolean mandatory;
    private Boolean multiValued;
    private Boolean searchable;
    private Boolean comparable;
    private Boolean visible;
    private Boolean editable;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;
}
