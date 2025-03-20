package com.nosql.poc.rules.exception;

import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Exception thrown when a rule validation fails
 */
@Getter
public class RuleValidationException extends RuntimeException {

    private final Map<String, Object> errors;

    public RuleValidationException(String message, Map<String, Object> errors) {
        super(message);
        this.errors = errors;
    }

    public RuleValidationException(String message, String fieldName, String errorDetail) {
        super(message);
        this.errors = Map.of(fieldName, errorDetail);
    }
    
    public RuleValidationException(String message, String fieldName, List<String> errorDetails) {
        super(message);
        this.errors = Map.of(fieldName, errorDetails);
    }
}