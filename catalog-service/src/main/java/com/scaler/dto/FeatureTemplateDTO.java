package com.scaler.dto;

import com.scaler.entity.FeatureType;
import com.scaler.entity.UnitOfMeasure;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)

public class FeatureTemplateDTO extends BaseDTO {
    private String templateType;
    private String name;
    private String description;
    private FeatureType featureType;
    private String dataType;
    private String inputType;
    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String defaultValue;
    private String metadata;
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
}
