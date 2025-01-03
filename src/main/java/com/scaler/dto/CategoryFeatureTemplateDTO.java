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
public class CategoryFeatureTemplateDTO {
    private UUID id;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Template ID is required")
    private UUID templateId;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Feature type is required")
    private String featureType;

    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String attributeType;
    private Boolean comparable = false;
    private Boolean visible = true;
    private Boolean searchable = false;
    private Boolean editable = true;
    private Boolean multiValued = false;
    private String defaultValue;
    private UUID unitId;
    private String metadata;
    private Boolean required = false;
    private String createdDate;
    private String lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

}
