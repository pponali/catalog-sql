package com.example.validation.fact;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationFact {
    private String featureCode;
    private String value;
    private String attributeType;
    private String validationPattern;
    private Double minValue;
    private Double maxValue;
    private String allowedValues;
    private String unit;
    private boolean isValid;
    private String errorMessage;

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public boolean isValid() {
        return isValid;
    }
}
