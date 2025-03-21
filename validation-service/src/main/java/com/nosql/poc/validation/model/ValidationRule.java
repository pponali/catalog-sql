package com.scaler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model representing a validation rule for use in validation logic.
 * This is a domain model used in the service layer, not persisted directly.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRule {
    private String id;
    
    private String code;
    private String name;
    private String description;
    private String ruleType;
    private String ruleExpression;
    private int priority;
    private String categoryId;
    private String featureId;
    private boolean active;
    private String pattern;
    private String allowedValues;
    private String minValue;
    private String maxValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum RuleType {
        REQUIRED,
        PATTERN,
        LENGTH,
        RANGE,
        DEPENDENCY,
        CUSTOM,
        ALLOWED_VALUES,
        TYPE
    }
    
    public String getRuleType() {
        return ruleType;
    }
    
    public String getPattern() {
        return ruleExpression;
    }
    
    public Double getMinValue() {
        if (ruleExpression == null) {
            return null;
        }
        
        String[] values = ruleExpression.split(",");
        if (values.length > 0 && !values[0].isEmpty()) {
            try {
                return Double.parseDouble(values[0]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public Double getMaxValue() {
        if (ruleExpression == null) {
            return null;
        }
        
        String[] values = ruleExpression.split(",");
        if (values.length > 1) {
            try {
                return Double.parseDouble(values[1]);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public List<String> getAllowedValues() {
        if (ruleExpression == null) {
            return List.of();
        }
        
        // Assuming allowed values are comma separated in ruleExpression
        String[] values = ruleExpression.split(",");
        return List.of(values);
    }
    
    public String getName() {
        return name;
    }
    
    public static ValidationRule createRequiredRule(String code, String name) {
        return ValidationRule.builder()
                .code(code)
                .name(name)
                .description("Field is required")
                .ruleType(RuleType.REQUIRED.toString())
                .ruleExpression("")
                .priority(0)
                .active(true)
                .build();
    }
    
    public static ValidationRule createPatternRule(String code, String name, String pattern) {
        return ValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must match pattern")
                .ruleType(RuleType.PATTERN.toString())
                .ruleExpression(pattern)
                .priority(0)
                .active(true)
                .build();
    }
    
    public static ValidationRule createRangeRule(String code, String name, String min, String max) {
        return ValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be within range")
                .ruleType(RuleType.RANGE.toString())
                .ruleExpression(String.format("%s,%s", min, max))
                .priority(0)
                .active(true)
                .build();
    }
    
    public static ValidationRule createAllowedValuesRule(String code, String name, String allowedValues) {
        return ValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be one of allowed values")
                .ruleType(RuleType.ALLOWED_VALUES.toString())
                .ruleExpression(allowedValues)
                .priority(0)
                .active(true)
                .build();
    }
    
    public static ValidationRule createTypeRule(String code, String name, String valueType) {
        return ValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be of the correct type")
                .ruleType("TYPE")
                .ruleExpression(valueType)
                .priority(0)
                .active(true)
                .build();
    }
    
    public static ValidationRule createLengthRule(String code, String name, Integer minLength, Integer maxLength) {
        return ValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be within valid length")
                .ruleType(RuleType.LENGTH.toString())
                .ruleExpression(String.format("%d,%d", minLength, maxLength))
                .priority(0)
                .active(true)
                .build();
    }
}