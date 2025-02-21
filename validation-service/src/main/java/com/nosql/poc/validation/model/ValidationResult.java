package com.nosql.poc.validation.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class ValidationResult {
    private boolean valid = true;
    private final List<ValidationError> errors = new CopyOnWriteArrayList<>();
    private final List<ValidationWarning> warnings = new CopyOnWriteArrayList<>();
    
    public synchronized void addError(String rule, String message) {
        errors.add(new ValidationError(rule, message));
        valid = false;
    }
    
    public synchronized void addError(ValidationError error) {
        errors.add(error);
        valid = false;
    }
    
    public synchronized void addWarning(String rule, String message) {
        warnings.add(new ValidationWarning(rule, message));
    }
    
    public synchronized void addWarning(ValidationWarning warning) {
        warnings.add(warning);
    }
    
    public List<ValidationError> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public List<ValidationWarning> getWarnings() {
        return new ArrayList<>(warnings);
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
    
    public void merge(ValidationResult other) {
        if (other == null) return;
        
        synchronized (this) {
            errors.addAll(other.getErrors());
            warnings.addAll(other.getWarnings());
            valid = valid && other.isValid();
        }
    }
}
