package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Represents a validation rule for channel data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRule {
    /**
     * Rule name.
     */
    private String name;
    
    /**
     * Rule type.
     */
    private String type; // REQUIRED_FIELD, FORMAT, RANGE, CUSTOM
    
    /**
     * Field to validate.
     */
    private String field;
    
    /**
     * Validation condition.
     */
    private String condition;
    
    /**
     * Error message on validation failure.
     */
    private String errorMessage;
    
    /**
     * Severity level.
     */
    private String severity; // ERROR, WARNING
    
    /**
     * Rule priority (lower numbers run first).
     */
    private int priority;
    
    /**
     * Additional parameters for the rule.
     */
    private Map<String, Object> parameters;
}
