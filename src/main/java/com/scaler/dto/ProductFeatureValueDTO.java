package com.scaler.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureValueDTO {
    private Long id;
    private Long productId;
    private Long featureId;
    private Long templateId;
    private String type;
    private String unit;
    private String unitOfMeasure;
    private String status;
    private String validationStatus;
    private String validationMessage;
    private String value;
    private JsonNode attributeValue;
    private ProductDTO product;
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
