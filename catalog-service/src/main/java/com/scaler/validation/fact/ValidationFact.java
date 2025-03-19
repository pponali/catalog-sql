package com.scaler.validation.fact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationFact {
    private String stringValue;
    private Double numericValue;
    private String pattern;
    private Double minValue;
    private Double maxValue;
    private List<String> allowedValues;
    private String message;
    private boolean valid;
    private String unit;
    private String errorMessage;
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    public Double getNumericValue() {
        return numericValue;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public Double getMinValue() {
        return minValue;
    }
    
    public Double getMaxValue() {
        return maxValue;
    }
    
    public List<String> getAllowedValues() {
        return allowedValues;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
