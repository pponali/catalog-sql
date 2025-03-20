package com.scaler.dto;

import com.scaler.entity.ValidationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for carrying validation results between services
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResultDTO extends BaseDTO {
    
    @NotNull(message = "Entity ID is required")
    private UUID entityId;
    
    @NotNull(message = "Entity type is required")
    private String entityType;
    
    private String fieldName;
    
    private ValidationStatus status;
    
    /**
     * Whether the validation passed (true) or failed (false)
     */
    private boolean valid;
    
    /**
     * List of validation error messages
     */
    @lombok.Builder.Default
    private List<String> errors = new ArrayList<>();

    /**
     * List of validation warning messages
     */
    @lombok.Builder.Default
    private List<String> warnings = new ArrayList<>();
    
    /**
     * Add an error message
     * 
     * @param error The error message to add
     * @return This ValidationResultDTO for chaining
     */
    public ValidationResultDTO addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
        status = ValidationStatus.FAILED;
        valid = false;
        return this;
    }
    
    /**
     * Add a warning message
     * 
     * @param warning The warning message to add
     * @return This ValidationResultDTO for chaining
     */
    public ValidationResultDTO addWarning(String warning) {
        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        warnings.add(warning);
        return this;
    }
    
    /**
     * Check if there are any validation errors
     * 
     * @return true if there are errors, false otherwise
     */
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
    
    /**
     * Check if there are any validation warnings
     * 
     * @return true if there are warnings, false otherwise
     */
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }
    
    /**
     * Combine another validation result with this one
     * 
     * @param other The other validation result to combine
     * @return This ValidationResultDTO with combined results
     */
    public ValidationResultDTO combine(ValidationResultDTO other) {
        if (other != null) {
            this.valid = this.valid && other.isValid();
            
            if (other.getStatus() == ValidationStatus.FAILED) {
                this.status = ValidationStatus.FAILED;
            }
            
            if (other.getErrors() != null) {
                if (this.errors == null) {
                    this.errors = new ArrayList<>();
                }
                this.errors.addAll(other.getErrors());
            }
            
            if (other.getWarnings() != null) {
                if (this.warnings == null) {
                    this.warnings = new ArrayList<>();
                }
                this.warnings.addAll(other.getWarnings());
            }
        }
        return this;
    }
    
    /**
     * Set the validation status based on error/warning state
     */
    public void updateStatus() {
        if (hasErrors()) {
            status = ValidationStatus.FAILED;
            valid = false;
        } else {
            // If there are no errors, it's considered PASSED
            status = ValidationStatus.PASSED;
            valid = true;
        }
    }
}
