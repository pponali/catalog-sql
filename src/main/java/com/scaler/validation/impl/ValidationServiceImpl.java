package com.scaler.validation.impl;

import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private static final Logger log = LoggerFactory.getLogger(ValidationServiceImpl.class);

    @Override
    public boolean validateValue(ProductFeatureValue value, CategoryFeatureTemplate template) {
        if (value == null || template == null) {
            log.debug("Value or template is null");
            return false;
        }

        String stringValue = value.getStringValue();
        String validationPattern = template.getValidationPattern();

        if (stringValue == null) {
            log.debug("Value is null for template: {}", template.getCode());
            return false;
        }

        try {
            return evaluatePattern(stringValue, validationPattern);
        } catch (Exception e) {
            log.error("Error validating value: {} against pattern: {}", value, validationPattern, e);
            return false;
        }
    }

    private boolean evaluatePattern(String value, String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            log.debug("No validation pattern specified, returning true");
            return true;
        }

        try {
            Pattern compiledPattern = Pattern.compile(pattern);
            boolean matches = compiledPattern.matcher(value).matches();
            log.debug("Value: {} {} match pattern: {}", value, matches ? "does" : "does not", pattern);
            return matches;
        } catch (Exception e) {
            log.error("Error evaluating pattern: {} for value: {}", pattern, value, e);
            return false;
        }
    }
}
