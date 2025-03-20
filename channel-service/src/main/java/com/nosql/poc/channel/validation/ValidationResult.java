package com.nosql.poc.channel.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Result of a validation operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {
    /**
     * Whether the validation passed.
     */
    private boolean valid;
    
    /**
     * List of validation errors if any.
     */
    private List<String> errors;
    
    /**
     * List of detailed validation errors.
     */
    private List<ValidationError> violations;
    
    /**
     * Creates a validation result with validity and errors.
     * 
     * @param valid whether the validation passed
     * @param errors list of error messages
     */
    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors != null ? errors : new ArrayList<>();
        this.violations = new ArrayList<>();
    }
    
    /**
     * Creates a successful validation result.
     * 
     * @return a valid result with no errors
     */
    public static ValidationResult success() {
        return new ValidationResult(true, Collections.emptyList());
    }
    
    /**
     * Creates a failed validation result.
     * 
     * @param error the error message
     * @return an invalid result with the error
     */
    public static ValidationResult failure(String error) {
        return new ValidationResult(false, Collections.singletonList(error));
    }
    
    /**
     * Creates a failed validation result.
     * 
     * @param errors the error messages
     * @return an invalid result with the errors
     */
    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, errors);
    }
    
    /**
     * Get the errors as a list of strings.
     * 
     * @return list of error messages
     */
    public List<String> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
            
            // If we have violations but no errors, convert them
            if (violations != null) {
                for (ValidationError violation : violations) {
                    errors.add(violation.getMessage());
                }
            }
        }
        
        return errors;
    }
}
