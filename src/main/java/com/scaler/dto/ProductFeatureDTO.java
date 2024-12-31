package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureDTO {
    private UUID id;

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

    private UUID unitId;

    @Builder.Default
    private boolean visible = true;

    @Builder.Default
    private boolean editable = true;

    @Builder.Default
    private boolean searchable = false;

    @Builder.Default
    private boolean comparable = false;

    @Builder.Default
    private boolean required = false;

    @Builder.Default
    private boolean multiValued = false;

    private String metadata;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;

    @Builder.Default
    private Set<ProductFeatureValueDTO> values = new HashSet<>();
}
