package com.example.validation.impl;

import com.example.entity.CategoryFeatureTemplate;
import com.example.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    @Override
    public boolean validateValue(CategoryFeatureTemplate template, String value) {
        // Check if value is required
        if (value == null || value.trim().isEmpty()) {
            return !template.isMandatory();
        }

        // Validate pattern if specified
        if (template.getValidationPattern() != null) {
            try {
                if (!Pattern.compile(template.getValidationPattern()).matcher(value).matches()) {
                    return false;
                }
            } catch (PatternSyntaxException e) {
                // Log invalid pattern error
                return false;
            }
        }

        // Validate numeric range if applicable
        if ("numeric".equalsIgnoreCase(template.getAttributeType())) {
            try {
                double numericValue = Double.parseDouble(value);
                if (template.getMinValue() != null && numericValue < template.getMinValue()) {
                    return false;
                }
                if (template.getMaxValue() != null && numericValue > template.getMaxValue()) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Validate boolean values
        if ("boolean".equalsIgnoreCase(template.getAttributeType())) {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                return false;
            }
        }

        // Validate enum values
        if ("enum".equalsIgnoreCase(template.getAttributeType())) {
            if (template.getAllowedValues() == null || template.getAllowedValues().isEmpty()) {
                return false;
            }
            return Arrays.asList(template.getAllowedValues().split(","))
                .stream()
                .map(String::trim)
                .anyMatch(v -> v.equals(value));
        }

        return true;
    }
}
