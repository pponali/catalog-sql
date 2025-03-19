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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductFeatureValueDTO extends BaseDTO {

    @NotNull(message = "Feature Mapping ID is required")
    private UUID featureId;

    private UUID productId;

    @NotNull(message = "Feature Template ID is required")
    private UUID featureTemplateId;

    @NotNull(message = "Type is required")
    private String type;
    private String unit;
    private String unitOfMeasure;
    private ValidationResultDTO validationResult;
    private String metadata;
    private String attributeValues;



    public JsonNode getAttributeValue() {
        return null;
    }

    public void setAttributeValue(JsonNode value) {
        this.attributeValues = value != null ? value.toString() : null;
    }
}
