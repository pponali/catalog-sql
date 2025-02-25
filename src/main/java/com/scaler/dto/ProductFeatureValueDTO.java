package com.scaler.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureValueDTO extends BaseDTO {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID featureId;

    @NotNull(message = "Feature Template ID is required")
    private UUID featureTemplateId;

    @NotNull(message = "Type is required")
    private String type;
    private String unit;
    private String unitOfMeasure;
    private ValidationResultDTO validationResult;
    private String metadata;
    private String attributeValues;

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
}
