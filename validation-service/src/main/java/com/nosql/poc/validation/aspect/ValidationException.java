package com.nosql.poc.validation.aspect;

import com.nosql.poc.validation.model.ValidationResult;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final ValidationResult validationResult;
    
    public ValidationException(String message, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
    }
}
