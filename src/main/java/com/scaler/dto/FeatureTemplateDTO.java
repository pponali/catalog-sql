package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureTemplateDTO {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Feature type is required")
    private String featureType;

    @NotBlank(message = "Data type is required")
    private String dataType;

    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String step;
    private String allowedValues;
    private String inputType;
    private Boolean required = false;
    private Boolean searchable = false;
    private Boolean filterable = false;
    private Boolean comparable = false;
    private Boolean hidden = false;
    private String defaultValue;
    private UUID unitId;
    private String metadata;

    private String createdDate;
    private String lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
