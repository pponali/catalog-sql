package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureDTO extends BaseDTO {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID templateId;

    @NotBlank(message = "Feature code is required")
    @Size(min = 2, max = 50, message = "Feature code must be between 2 and 50 characters")
    private String code;

    @NotBlank(message = "Feature name is required")
    @Size(min = 2, max = 255, message = "Feature name must be between 2 and 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Attribute type is required")
    private String attributeType;

    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String defaultValue;

    @NotBlank(message = "Feature type is required")
    private String featureType;

    private UUID unitOfMeasureId;

    /**
     * Whether the feature is visible or not
     */
    private boolean visible = true;

    /**
     * Whether the feature is editable or not
     */
    private boolean editable = true;

    /**
     * Whether the feature is searchable or not
     */
    private boolean searchable = false;

    /**
     * Whether the feature is comparable or not
     */
    private boolean comparable = false;

    /**
     * Whether the feature is required or not
     */
    private boolean required = false;

    /**
     * Whether the feature is multi-valued or not
     */
    private boolean multiValued = false;

    private String metadata;

    private String createdDate;
    private String lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    /**
     * Set of feature values
     */
    private Set<ProductFeatureValueDTO> featureValues = new HashSet<>();
}
