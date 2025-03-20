package com.nosql.poc.validation.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.nosql.poc.validation.model.SimpleValidationRule;
import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;
import com.nosql.poc.validation.model.ValidationSeverity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Core validation engine that applies validation rules to entities.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationEngine {
    
    /**
     * Validates an entity against a set of rules.
     *
     * @param context the validation context containing the entity and rules
     * @return a validation result with details about validation status
     */
    public ValidationResult validate(ValidationContext context) {
        log.debug("Validating {} entity", context.getEntityType());
        
        ValidationResult result = new ValidationResult();
        result.setEntityType(context.getEntityType());
        result.setValid(true);
        
        // Apply each rule to the entity
        for (SimpleValidationRule rule : context.getRules()) {
            if (Boolean.TRUE.equals(rule.getActive())) {
                validateRule(rule, context.getEntity(), result);
            }
        }
        
        return result;
    }
    
    /**
     * Applies a single validation rule to an entity.
     *
     * @param rule the validation rule to apply
     * @param entity the entity to validate
     * @param result the validation result to update
     */
    private void validateRule(SimpleValidationRule rule, JsonNode entity, ValidationResult result) {
        log.debug("Applying rule: {}", rule.getName());
        
        // Get the attribute value from the entity
        JsonNode attributeNode = entity.get(rule.getAttribute());
        
        // Skip validation if the attribute is not present and not required
        if (attributeNode == null && !rule.getCondition().equals("NOT_NULL")) {
            return;
        }
        
        String attributeValue = attributeNode != null ? attributeNode.asText() : null;
        boolean isValid = true;
        
        // Apply the condition
        switch (rule.getCondition()) {
            case "NOT_NULL":
                isValid = attributeNode != null && !attributeNode.isNull();
                break;
                
            case "NOT_EMPTY":
                isValid = attributeNode != null && !attributeNode.isNull() && 
                        !StringUtils.isEmpty(attributeValue);
                break;
                
            case "REGEX":
                isValid = validateRegex(attributeValue, rule.getConditionValue());
                break;
                
            case "MIN_LENGTH":
                isValid = validateMinLength(attributeValue, rule.getConditionValue());
                break;
                
            case "MAX_LENGTH":
                isValid = validateMaxLength(attributeValue, rule.getConditionValue());
                break;
                
            case "MIN_VALUE":
                isValid = validateMinValue(attributeValue, rule.getConditionValue());
                break;
                
            case "MAX_VALUE":
                isValid = validateMaxValue(attributeValue, rule.getConditionValue());
                break;
                
            case "ENUM":
                isValid = validateEnum(attributeValue, rule.getConditionValue());
                break;
                
            default:
                log.warn("Unknown condition: {}", rule.getCondition());
                break;
        }
        
        // Update the validation result
        if (!isValid) {
            if (rule.getSeverity().equals("ERROR")) {
                result.setValid(false);
                result.getErrorMessages().add(rule.getMessage());
                result.addError(rule.getName(), rule.getMessage());
            } else if (rule.getSeverity().equals("WARNING")) {
                result.getWarningMessages().add(rule.getMessage());
                result.addWarning(rule.getName(), rule.getMessage());
            }
        }
    }
    
    /**
     * Validates a value against a regular expression.
     *
     * @param value the value to validate
     * @param regex the regular expression
     * @return true if the value matches the regex, false otherwise
     */
    private boolean validateRegex(String value, String regex) {
        if (value == null) {
            return false;
        }
        
        try {
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(value).matches();
        } catch (PatternSyntaxException e) {
            log.error("Invalid regex pattern: {}", regex, e);
            return false;
        }
    }
    
    /**
     * Validates that a value has at least a minimum length.
     *
     * @param value the value to validate
     * @param minLengthStr the minimum length as a string
     * @return true if the value is at least the minimum length, false otherwise
     */
    private boolean validateMinLength(String value, String minLengthStr) {
        if (value == null) {
            return false;
        }
        
        try {
            int minLength = Integer.parseInt(minLengthStr);
            return value.length() >= minLength;
        } catch (NumberFormatException e) {
            log.error("Invalid min length: {}", minLengthStr, e);
            return false;
        }
    }
    
    /**
     * Validates that a value does not exceed a maximum length.
     *
     * @param value the value to validate
     * @param maxLengthStr the maximum length as a string
     * @return true if the value does not exceed the maximum length, false otherwise
     */
    private boolean validateMaxLength(String value, String maxLengthStr) {
        if (value == null) {
            return true; // Null is considered valid for max length
        }
        
        try {
            int maxLength = Integer.parseInt(maxLengthStr);
            return value.length() <= maxLength;
        } catch (NumberFormatException e) {
            log.error("Invalid max length: {}", maxLengthStr, e);
            return false;
        }
    }
    
    /**
     * Validates that a numeric value is at least a minimum value.
     *
     * @param value the value to validate
     * @param minValueStr the minimum value as a string
     * @return true if the value is at least the minimum value, false otherwise
     */
    private boolean validateMinValue(String value, String minValueStr) {
        if (value == null) {
            return false;
        }
        
        try {
            double actualValue = Double.parseDouble(value);
            double minValue = Double.parseDouble(minValueStr);
            return actualValue >= minValue;
        } catch (NumberFormatException e) {
            log.error("Invalid numeric value for min value validation: {} or {}", value, minValueStr, e);
            return false;
        }
    }
    
    /**
     * Validates that a numeric value does not exceed a maximum value.
     *
     * @param value the value to validate
     * @param maxValueStr the maximum value as a string
     * @return true if the value does not exceed the maximum value, false otherwise
     */
    private boolean validateMaxValue(String value, String maxValueStr) {
        if (value == null) {
            return true; // Null is considered valid for max value
        }
        
        try {
            double actualValue = Double.parseDouble(value);
            double maxValue = Double.parseDouble(maxValueStr);
            return actualValue <= maxValue;
        } catch (NumberFormatException e) {
            log.error("Invalid numeric value for max value validation: {} or {}", value, maxValueStr, e);
            return false;
        }
    }
    
    /**
     * Validates that a value is one of a set of allowed values.
     *
     * @param value the value to validate
     * @param allowedValuesStr the allowed values as a comma-separated string
     * @return true if the value is one of the allowed values, false otherwise
     */
    private boolean validateEnum(String value, String allowedValuesStr) {
        if (value == null) {
            return false;
        }
        
        String[] allowedValues = allowedValuesStr.split(",");
        for (String allowedValue : allowedValues) {
            if (value.equals(allowedValue.trim())) {
                return true;
            }
        }
        
        return false;
    }
}