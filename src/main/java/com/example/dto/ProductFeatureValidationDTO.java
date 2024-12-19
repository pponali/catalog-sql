package com.example.dto;

import com.example.enums.FeatureValueType;
import com.example.validation.ValidFeatureValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductFeatureValidationDTO {
    @NotBlank(message = "Feature code is required")
    private String code;

    @NotBlank(message = "Feature name is required")
    private String name;

    @NotNull(message = "Value type is required")
    private FeatureValueType valueType;

    @ValidFeatureValue(message = "Invalid feature value format")
    private List<String> values;

    // Custom validation method to ensure values match the valueType
    public boolean isValidValueType() {
        if (values == null || values.isEmpty()) {
            return true;
        }

        switch (valueType) {
            case STRING:
            case NUMERIC:
            case BOOLEAN:
                return values.size() == 1;
            case STRING_LIST:
            case NUMERIC_LIST:
            case BOOLEAN_LIST:
                return true;
            default:
                return false;
        }
    }
}
