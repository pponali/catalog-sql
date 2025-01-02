package com.scaler.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureValueDTO {
    private UUID id;

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID featureId;

    @NotNull(message = "Feature Template ID is required")
    private UUID featureTemplateId;

    private String type;
    private String unit;
    private String unitOfMeasure;
    private String status;
    private String validationStatus;
    private String validationPattern;
    private String validationMessage;

    private String value;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;

    @ToString.Exclude
    private ProductDTO product;

    @ToString.Exclude
    private ProductFeatureDTO feature;

    public JsonNode getAttributeValue() {
        return null;
    }

    public void setAttributeValue(JsonNode value) {
    }

    public String getStringValue() {
        return value;
    }

    public Double getNumericValue() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean getBooleanValue() {
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }
}
