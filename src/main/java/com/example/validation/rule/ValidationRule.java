package com.example.validation.rule;

import com.example.entity.CategoryFeatureTemplate;
import lombok.Builder;
import lombok.Data;

import java.util.function.Predicate;

@Data
@Builder
public class ValidationRule {
    private String code;
    private String name;
    private String description;
    private RuleType ruleType;
    private String pattern;
    private Double minValue;
    private Double maxValue;
    private String[] allowedValues;

    public enum RuleType {
        REQUIRED,
        TYPE,
        RANGE,
        PATTERN,
        ENUM
    }

    public static ValidationRule createRequiredRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
            .code("REQUIRED")
            .name("Required Field")
            .description("Field is required")
            .ruleType(RuleType.REQUIRED)
            .build();
    }

    public static ValidationRule createTypeRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
            .code("TYPE")
            .name("Type Validation")
            .description("Value must match type " + template.getAttributeType())
            .ruleType(RuleType.TYPE)
            .pattern(getTypePattern(template.getAttributeType()))
            .build();
    }

    public static ValidationRule createRangeRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
            .code("RANGE")
            .name("Range Validation")
            .description(String.format("Value must be between %s and %s", 
                template.getMinValue(), template.getMaxValue()))
            .ruleType(RuleType.RANGE)
            .minValue(template.getMinValue())
            .maxValue(template.getMaxValue())
            .build();
    }

    public static ValidationRule createRegexRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
            .code("PATTERN")
            .name("Pattern Validation")
            .description("Value must match pattern " + template.getValidationPattern())
            .ruleType(RuleType.PATTERN)
            .pattern(template.getValidationPattern())
            .build();
    }

    public boolean validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ruleType != RuleType.REQUIRED;
        }

        return switch (ruleType) {
            case REQUIRED -> true;
            case TYPE -> value.matches(pattern);
            case RANGE -> validateRange(value);
            case PATTERN -> value.matches(pattern);
            case ENUM -> validateEnum(value);
        };
    }

    private boolean validateRange(String value) {
        try {
            double numericValue = Double.parseDouble(value);
            if (minValue != null && numericValue < minValue) {
                return false;
            }
            if (maxValue != null && numericValue > maxValue) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateEnum(String value) {
        if (allowedValues == null) {
            return true;
        }
        for (String allowed : allowedValues) {
            if (allowed.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private static String getTypePattern(String attributeType) {
        return switch (attributeType.toLowerCase()) {
            case "string" -> ".*";
            case "numeric" -> "^-?\\d*\\.?\\d+$";
            case "boolean" -> "^(true|false)$";
            case "enum" -> ".*";
            default -> ".*";
        };
    }
}
