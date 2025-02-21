package com.nosql.poc.validation.rules;

import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;
import lombok.Getter;
import org.springframework.core.Ordered;

@Getter
public abstract class AbstractValidationRule implements ValidationRule, Ordered {
    
    private final String name;
    private final Class<?> targetClass;
    private final int priority;
    private final boolean async;
    
    protected AbstractValidationRule(String name, Class<?> targetClass, int priority, boolean async) {
        this.name = name;
        this.targetClass = targetClass;
        this.priority = priority;
        this.async = async;
    }
    
    @Override
    public int getOrder() {
        return priority;
    }
    
    @Override
    public abstract void validate(Object target, ValidationContext context, ValidationResult result);
    
    protected void addError(ValidationResult result, String message) {
        result.addError(getName(), message);
    }
    
    protected void addWarning(ValidationResult result, String message) {
        result.addWarning(getName(), message);
    }
}
