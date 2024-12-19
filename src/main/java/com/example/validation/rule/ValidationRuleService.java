package com.example.validation.rule;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ProductFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationRuleService {

    public List<ValidationRule> getValidationRules(ProductFeature feature) {
        List<ValidationRule> rules = new ArrayList<>();
        CategoryFeatureTemplate template = feature.getTemplate();

        // Required validation
        if (template.isMandatory()) {
            rules.add(ValidationRule.createRequiredRule(template));
        }

        // Type validation
        rules.add(ValidationRule.createTypeRule(template));

        // Range validation for numeric values
        if ("numeric".equalsIgnoreCase(template.getAttributeType()) &&
            (template.getMinValue() != null || template.getMaxValue() != null)) {
            rules.add(ValidationRule.createRangeRule(template));
        }

        // Pattern validation for strings
        if ("string".equalsIgnoreCase(template.getAttributeType()) &&
            template.getValidationPattern() != null) {
            ValidationRule rule = ValidationRule.builder()
                .code("PATTERN")
                .name("Pattern Validation")
                .description("Value must match pattern " + template.getValidationPattern())
                .ruleType(ValidationRule.RuleType.PATTERN)
                .pattern(template.getValidationPattern())
                .build();
            rules.add(rule);
        }

        return rules;
    }

    public boolean validateFeature(ProductFeature feature) {
        List<ValidationRule> rules = getValidationRules(feature);
        
        // If no values and feature is not required, it's valid
        if (feature.getValues().isEmpty() && !feature.getTemplate().isMandatory()) {
            return true;
        }

        // Check multi-value constraint
        if (!feature.getTemplate().isMultiValued() && feature.getValues().size() > 1) {
            return false;
        }

        // Validate each value against all rules
        return feature.getValues().stream()
            .allMatch(value -> validateValue(value.getValue(), rules));
    }

    private boolean validateValue(String value, List<ValidationRule> rules) {
        return rules.stream().allMatch(rule -> validateRule(rule, value));
    }

    private boolean validateRule(ValidationRule rule, String value) {
        if (value == null || value.trim().isEmpty()) {
            return rule.getRuleType() != ValidationRule.RuleType.REQUIRED;
        }

        return switch (rule.getRuleType()) {
            case REQUIRED -> true;
            case TYPE -> value.matches(rule.getPattern());
            case RANGE -> validateRange(value, rule);
            case PATTERN -> value.matches(rule.getPattern());
            case ENUMERATION -> validateEnum(value, rule);
            case LENGTH -> validateLength(value, rule);
        };
    }

    private boolean validateRange(String value, ValidationRule rule) {
        try {
            double numericValue = Double.parseDouble(value);
            if (rule.getMinValue() != null && numericValue < rule.getMinValue()) {
                return false;
            }
            return rule.getMaxValue() == null || numericValue <= rule.getMaxValue();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateEnum(String value, ValidationRule rule) {
        List<String> allowedValues = rule.getAllowedValues();
        return allowedValues.stream()
            .map(String::trim)
            .anyMatch(allowed -> allowed.equals(value));
    }

    private boolean validateLength(String value, ValidationRule rule) {
        int length = value.length();
        if (rule.getMinLength() != null && length < rule.getMinLength()) {
            return false;
        }
        return rule.getMaxLength() == null || length <= rule.getMaxLength();
    }
}
