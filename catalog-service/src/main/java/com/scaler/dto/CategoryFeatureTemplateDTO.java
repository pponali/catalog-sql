package com.scaler.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
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
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFeatureTemplateDTO extends BaseDTO {
    private String version;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    private UUID featureId;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Type is required")
    private String featureType;

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
    private String dataType = "STRING";
    private String inputType = "STRING";

    private boolean visible = true;
    private boolean editable = true;
    private boolean searchable = true;
    private boolean comparable = true;
    private boolean mandatory = false;
    private boolean multiValued = false;
    private boolean filterable = true;
    private boolean hidden = false;
    private boolean inherited = false;

    private String metadata = "";


    @JsonIgnore
    private Set<ProductFeatureDTO> features = new HashSet<>();
}
