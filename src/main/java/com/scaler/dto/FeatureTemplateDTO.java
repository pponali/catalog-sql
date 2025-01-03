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
    private UUID unitId;
    private String description;
    private String featuretype;
    private String datatype;
    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String defaultValue;
    private Boolean required;
    private Boolean multiValued;
    private Boolean searchable;
    private Boolean filterable;
    private Boolean hidden;
    private Boolean comparable;
    private Boolean visible;
    private Boolean editable;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;
}
