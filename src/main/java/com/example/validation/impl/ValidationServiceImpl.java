package com.example.validation.impl;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ProductFeatureValue;
import com.example.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    @Override
    public boolean validateValue(ProductFeatureValue value, CategoryFeatureTemplate template) {
        String stringValue = value.getValue();
        if (stringValue == null || stringValue.trim().isEmpty()) {
            return !template.isMandatory();
        }

        switch (template.getAttributeType().toLowerCase()) {
            case "string":
                return validateString(stringValue, template);
            case "numeric":
                return validateNumeric(stringValue, template);
            case "enum":
                return validateEnum(stringValue, template);
            default:
                return false;
        }
    }

    private boolean validateString(String value, CategoryFeatureTemplate template) {
        if (template.getValidationPattern() != null && !template.getValidationPattern().isEmpty()) {
            return Pattern.compile(template.getValidationPattern()).matcher(value).matches();
        }
        return true;
    }

    private boolean validateNumeric(String value, CategoryFeatureTemplate template) {
        try {
            double numericValue = Double.parseDouble(value);
            if (template.getMinValue() != null && numericValue < template.getMinValue()) {
                return false;
            }
            if (template.getMaxValue() != null && numericValue > template.getMaxValue()) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateEnum(String value, CategoryFeatureTemplate template) {
        if (template.getAllowedValues() == null || template.getAllowedValues().isEmpty()) {
            return false;
        }
        String[] allowedValues = template.getAllowedValues().split(",");
        for (String allowedValue : allowedValues) {
            if (allowedValue.trim().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
