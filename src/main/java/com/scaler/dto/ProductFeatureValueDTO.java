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

    @NotNull(message = "Feature ID is required")
    private UUID featureId;

    private UUID templateId;

    @NotNull(message = "Value type is required")
    private String type;

    private String unit;
    private String unitOfMeasure;
    private String status;
    private String validationStatus;
    private String validationPattern;
    private String validationMessage;
    private String value;

    @NotNull(message = "Attribute value is required")
    private JsonNode attributeValue;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;

    @ToString.Exclude
    private ProductDTO product;

    @ToString.Exclude
    private ProductFeatureDTO feature;

    public JsonNode getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(JsonNode value) {
        this.attributeValue = value;
    }

    public String getStringValue() {
        if (attributeValue != null && attributeValue.isTextual()) {
            return attributeValue.asText();
        }
        return value;
    }

    public Double getNumericValue() {
        if (attributeValue != null && attributeValue.isNumber()) {
            return attributeValue.asDouble();
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Boolean getBooleanValue() {
        if (attributeValue != null && attributeValue.isBoolean()) {
            return attributeValue.asBoolean();
        }
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }
}
