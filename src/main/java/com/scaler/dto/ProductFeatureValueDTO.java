package com.scaler.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
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

    @NotNull(message = "Type is required")
    private String type;
    private String unit;
    private String unitOfMeasure;
    private String status;
    private String validationStatus;
    private String validationPattern;
    private String validationMessage;
    private String metadata;
    private String attributeValues;

    private String createdDate;
    private String lastModifiedDate;
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
        this.attributeValues = value != null ? value.toString() : null;
    }

    public void setValidationStatus(String status) {
        this.validationStatus = status;
    }

    public void setValidationMessage(String message) {
        this.validationMessage = message;
    }
}
