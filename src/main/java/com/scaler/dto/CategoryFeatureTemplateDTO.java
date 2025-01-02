package com.scaler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFeatureTemplateDTO {
    private UUID id;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    private UUID featureId;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Type is required")
    private String type;

    private UUID unitId;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Attribute type is required")
    private String attributeType;

    private String validationPattern = "";
    private String minValue = "";
    private String maxValue = "";
    private String allowedValues = "";
    private String defaultValue = "";

    @NotBlank(message = "Feature type is required")
    private String featureType = "STRING";

    private boolean visible = true;
    private boolean editable = true;
    private boolean searchable = true;
    private boolean comparable = true;
    private boolean mandatory = false;
    private boolean multiValued = false;

    private String metadata = "";

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    @JsonIgnore
    private Set<ProductFeatureDTO> features = new HashSet<>();
}
