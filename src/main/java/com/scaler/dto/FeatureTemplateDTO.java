package com.scaler.dto;

import com.scaler.entity.UnitOfMeasure;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureTemplateDTO {
    private UUID id;
    private String templateType;
    private String name;
    private String description;
    private String featureType;
    private String dataType;
    private String inputType;
    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String defaultValue;
    private UUID unitId;
    private UnitOfMeasure unit;
    private Boolean required = false;
    private Boolean filterable = true;
    private Boolean hidden = false;
    private Boolean multiValued = false;
    private Boolean searchable = true;
    private Boolean comparable = true;
    private Boolean visible = true;
    private Boolean editable = true;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;
}
