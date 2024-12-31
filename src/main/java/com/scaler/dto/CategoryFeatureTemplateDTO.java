package com.scaler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CategoryFeatureTemplateDTO {
    private UUID id;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    private UUID unitId;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Attribute type is required")
    private String attributeType;

    @Builder.Default
    private String validationPattern = "";

    @Builder.Default
    private String minValue = "";

    @Builder.Default
    private String maxValue = "";

    @Builder.Default
    private String allowedValues = "";

    @Builder.Default
    private String defaultValue = "";

    @NotBlank(message = "Feature type is required")
    @Builder.Default
    private String featureType = "STRING";

    @Builder.Default
    private boolean visible = true;

    @Builder.Default
    private boolean editable = true;

    @Builder.Default
    private boolean searchable = true;

    @Builder.Default
    private boolean comparable = true;

    @Builder.Default
    private boolean mandatory = false;

    @Builder.Default
    private boolean multiValued = false;

    @Builder.Default
    private String metadata = "";

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;

    @JsonIgnore
    @Builder.Default
    private Set<ProductFeatureDTO> features = new HashSet<>();
}
