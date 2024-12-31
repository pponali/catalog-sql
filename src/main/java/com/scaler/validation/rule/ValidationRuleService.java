package com.scaler.validation.rule;

import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.model.ValidationRule;
import com.scaler.repository.ValidationRuleRepository;
import com.scaler.service.validation.ValidationEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationRuleService {

    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationEngine validationEngine;

    public List<ValidationRule> getRulesForFeature(ProductFeature feature) {
        List<ValidationRule> rules = new ArrayList<>();
        
        // Add required rule if feature is mandatory
        if (feature.isRequired()) {
            rules.add(createRequiredRule(feature));
        }
        
        // Add pattern rule if validation pattern exists
        if (feature.getValidationPattern() != null) {
            rules.add(createPatternRule(feature));
        }
        
        // Add range rule if min/max values exist
        if (feature.getMinValue() != null || feature.getMaxValue() != null) {
            rules.add(createRangeRule(feature));
        }
        
        // Add allowed values rule if allowed values exist
        if (feature.getAllowedValues() != null && !feature.getAllowedValues().isEmpty()) {
            rules.add(createAllowedValuesRule(feature));
        }
        
        return rules;
    }

    public void validate(ProductFeatureValue value, ValidationRule rule) {
        if (!validateRule(rule, value.getStringValue())) {
            throw new IllegalArgumentException("Validation failed for rule: " + rule.getRuleType());
        }
    }

    public ValidationRule createRequiredRule(ProductFeature feature) {
        return ValidationRule.builder()
                .code(ValidationEngine.REQUIRED)
                .name("Required Field")
                .ruleType(ValidationEngine.REQUIRED)
                .feature(feature)
                .build();
    }

    public ValidationRule createPatternRule(ProductFeature feature) {
        return ValidationRule.builder()
                .code(ValidationEngine.PATTERN)
                .name("Pattern Validation")
                .ruleType(ValidationEngine.PATTERN)
                .feature(feature)
                .pattern(feature.getValidationPattern())
                .build();
    }

    public ValidationRule createRangeRule(ProductFeature feature) {
        return ValidationRule.builder()
                .code(ValidationEngine.RANGE)
                .name("Range Validation")
                .ruleType(ValidationEngine.RANGE)
                .feature(feature)
                .minValue(feature.getMinValue())
                .maxValue(feature.getMaxValue())
                .build();
    }

    public ValidationRule createAllowedValuesRule(ProductFeature feature) {
        return ValidationRule.builder()
                .code(ValidationEngine.ALLOWED_VALUES)
                .name("Allowed Values")
                .ruleType(ValidationEngine.ALLOWED_VALUES)
                .feature(feature)
                .allowedValues(feature.getAllowedValues())
                .build();
    }

    public boolean validateRule(ValidationRule rule, String value) {
        if (rule.getRuleType().equals(ValidationEngine.REQUIRED)) {
            return value != null && !value.trim().isEmpty();
        } else if (rule.getRuleType().equals(ValidationEngine.PATTERN)) {
            return value != null && value.matches(rule.getPattern());
        } else if (rule.getRuleType().equals(ValidationEngine.RANGE)) {
            double val = Double.parseDouble(value);
            return val >= rule.getMinValue() && val <= rule.getMaxValue();
        } else if (rule.getRuleType().equals(ValidationEngine.ALLOWED_VALUES)) {
            return value != null && rule.getAllowedValues().contains(value);
        }
        return true;
    }
}
