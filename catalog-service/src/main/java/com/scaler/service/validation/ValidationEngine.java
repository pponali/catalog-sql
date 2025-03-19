package com.scaler.service.validation;

import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ValidationEngine {

    public static final String REQUIRED = "REQUIRED";
    public static final String PATTERN = "PATTERN";
    public static final String RANGE = "RANGE";
    public static final String ALLOWED_VALUES = "ALLOWED_VALUES";

    public List<String> validate(ProductFeatureValue value, ProductFeature feature) {
        List<String> violations = new ArrayList<>();
        
        if (value.getAttributeValues() == null) {
            if (feature.isRequired()) {
                violations.add("Value is required");
            }
            return violations;
        }

        String stringValue = value.getAttributeValues().asText();
        
        // Required validation
        if (feature.isRequired() && stringValue.isEmpty()) {
            violations.add("Value is required");
        }
        
        // Pattern validation
        if (feature.getValidationPattern() != null) {
            if (!Pattern.matches(feature.getValidationPattern(), stringValue)) {
                violations.add("Value does not match the required pattern: " + feature.getValidationPattern());
            }
        }
        
        // Range validation
        if (value.getAttributeValues().isNumber() && (feature.getMinValue() != null || feature.getMaxValue() != null)) {
            double numericValue = value.getAttributeValues().asDouble();
            Double minValue = feature.getMinValue() != null ? Double.parseDouble(feature.getMinValue()) : null;
            Double maxValue = feature.getMaxValue() != null ? Double.parseDouble(feature.getMaxValue()) : null;
            
            if (minValue != null && numericValue < minValue) {
                violations.add("Value is less than minimum allowed: " + minValue);
            }
            if (maxValue != null && numericValue > maxValue) {
                violations.add("Value is greater than maximum allowed: " + maxValue);
            }
        }
        
        // Allowed values validation
        if (feature.getAllowedValues() != null && !feature.getAllowedValues().isEmpty()) {
            if (!feature.getAllowedValues().contains(stringValue)) {
                violations.add("Value is not in the list of allowed values: " + String.join(", ", feature.getAllowedValues()));
            }
        }
        
        return violations;
    }
    
    public boolean isRequired(ProductFeature feature) {
        return feature.isRequired();
    }
}
