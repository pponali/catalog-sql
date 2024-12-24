package com.scaler.validation.rule;

import com.scaler.entity.CategoryFeatureTemplate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationRule {
    private String code;
    private String name;
    private String description;
    private String ruleType;
    private String pattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String errorMessage;

    public static ValidationRule createRequiredRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code(template.getCode() + "_REQUIRED")
                .name(template.getName() + " Required")
                .description("Validates that " + template.getName() + " is not empty")
                .ruleType("REQUIRED")
                .errorMessage("Value is required")
                .build();
    }

    public static ValidationRule createTypeRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code(template.getCode() + "_TYPE")
                .name(template.getName() + " Type")
                .description("Validates that " + template.getName() + " is of type " + template.getAttributeType())
                .ruleType("TYPE")
                .pattern(template.getAttributeType())
                .errorMessage("Invalid type. Expected: " + template.getAttributeType())
                .build();
    }

    public static ValidationRule createRangeRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code(template.getCode() + "_RANGE")
                .name(template.getName() + " Range")
                .description("Validates that " + template.getName() + " is between " + template.getMinValue() + " and " + template.getMaxValue())
                .ruleType("RANGE")
                .minValue(template.getMinValue())
                .maxValue(template.getMaxValue())
                .errorMessage("Value must be between " + template.getMinValue() + " and " + template.getMaxValue())
                .build();
    }

    public static ValidationRule createPatternRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code(template.getCode())
                .name(template.getName())
                .description("Pattern validation for " + template.getName())
                .ruleType("PATTERN")
                .pattern(template.getValidationPattern())
                .errorMessage("Value does not match required pattern")
                .build();
    }

    public static ValidationRule createAllowedValuesRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code(template.getCode())
                .name(template.getName())
                .description("Allowed values validation for " + template.getName())
                .ruleType("ALLOWED_VALUES")
                .allowedValues(template.getAllowedValues())
                .errorMessage("Value is not in the list of allowed values")
                .build();
    }

    public static ValidationRule createCustomRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code(template.getCode())
                .name(template.getName())
                .description("Custom validation for " + template.getName())
                .ruleType("CUSTOM")
                .pattern(template.getValidationPattern())
                .errorMessage("Value does not meet custom validation requirements")
                .build();
    }
}
