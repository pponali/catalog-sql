package com.nosql.poc.validation.rules;

import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;

public interface ValidationRule {
    String getName();
    
    Class<?> getTargetClass();
    
    int getPriority();
    
    boolean isAsync();
    
    void validate(Object target, ValidationContext context, ValidationResult result);
    
    default boolean supports(Object target, ValidationContext context) {
        return getTargetClass().isInstance(target);
    }
}
