package com.nosql.poc.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Result of a validation operation, including validation status and any errors, warnings, or info messages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {
    private boolean valid = true;
    private String entityType;
    private final List<ValidationError> errors = new CopyOnWriteArrayList<>();
    private final List<ValidationWarning> warnings = new CopyOnWriteArrayList<>();
    private final List<ValidationInfo> infoList = new CopyOnWriteArrayList<>();
    private final List<String> errorMessages = new CopyOnWriteArrayList<>();
    private final List<String> warningMessages = new CopyOnWriteArrayList<>();
    private final List<String> infoMessages = new CopyOnWriteArrayList<>();
    private LocalDateTime validationTime = LocalDateTime.now();
    
    // Track rules that were applied
    private final List<String> appliedRules = new CopyOnWriteArrayList<>();
    
    // Track rules that failed
    private final List<String> failedRules = new CopyOnWriteArrayList<>();
    
    // Additional validation metadata
    private Map<String, Object> metadata = new HashMap<>();
    
    /**
     * Adds an error to the validation result.
     *
     * @param rule the rule that generated the error
     * @param message the error message
     */
    public synchronized void addError(String rule, String message) {
        errors.add(new ValidationError(rule, message));
        errorMessages.add(message);
        failedRules.add(rule);
        valid = false;
    }
    
    /**
     * Adds an error to the validation result.
     *
     * @param error the validation error
     */
    public synchronized void addError(ValidationError error) {
        errors.add(error);
        errorMessages.add(error.getMessage());
        failedRules.add(error.getRule());
        valid = false;
    }
    
    /**
     * Adds a warning to the validation result.
     *
     * @param rule the rule that generated the warning
     * @param message the warning message
     */
    public synchronized void addWarning(String rule, String message) {
        warnings.add(new ValidationWarning(rule, message));
        warningMessages.add(message);
    }
    
    /**
     * Adds a warning to the validation result.
     *
     * @param warning the validation warning
     */
    public synchronized void addWarning(ValidationWarning warning) {
        warnings.add(warning);
        warningMessages.add(warning.getMessage());
    }
    
    /**
     * Adds an info message to the validation result.
     *
     * @param rule the rule that generated the info message
     * @param message the info message
     */
    public synchronized void addInfo(String rule, String message) {
        infoList.add(new ValidationInfo(rule, message));
        infoMessages.add(message);
    }
    
    /**
     * Adds an info message to the validation result.
     *
     * @param info the validation info
     */
    public synchronized void addInfo(ValidationInfo info) {
        infoList.add(info);
        infoMessages.add(info.getMessage());
    }
    
    /**
     * Records that a rule was applied during validation.
     *
     * @param rule the rule that was applied
     */
    public synchronized void addAppliedRule(String rule) {
        appliedRules.add(rule);
    }
    
    /**
     * Adds metadata to the validation result.
     *
     * @param key the metadata key
     * @param value the metadata value
     */
    public synchronized void addMetadata(String key, Object value) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put(key, value);
    }
    
    /**
     * Gets the validation errors.
     *
     * @return the validation errors
     */
    public List<ValidationError> getErrors() {
        return new ArrayList<>(errors);
    }
    
    /**
     * Gets the validation warnings.
     *
     * @return the validation warnings
     */
    public List<ValidationWarning> getWarnings() {
        return new ArrayList<>(warnings);
    }
    
    /**
     * Gets the validation info messages.
     *
     * @return the validation info messages
     */
    public List<ValidationInfo> getInfos() {
        return new ArrayList<>(infoList);
    }
    
    /**
     * Checks if the validation result has errors.
     *
     * @return true if there are errors, false otherwise
     */
    public boolean hasErrors() {
        return !errors.isEmpty() || !errorMessages.isEmpty();
    }
    
    /**
     * Checks if the validation result has warnings.
     *
     * @return true if there are warnings, false otherwise
     */
    public boolean hasWarnings() {
        return !warnings.isEmpty() || !warningMessages.isEmpty();
    }
    
    /**
     * Checks if the validation result has info messages.
     *
     * @return true if there are info messages, false otherwise
     */
    public boolean hasInfos() {
        return !infoMessages.isEmpty() || !infoList.isEmpty();
    }
    
    /**
     * Gets the count of applied rules.
     *
     * @return the count of applied rules
     */
    public int getAppliedRulesCount() {
        return appliedRules.size();
    }
    
    /**
     * Gets the count of failed rules.
     *
     * @return the count of failed rules
     */
    public int getFailedRulesCount() {
        return failedRules.size();
    }
    
    /**
     * Gets a summary of the validation result.
     *
     * @return a summary of the validation result
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Validation Result for ").append(entityType).append(":\n");
        sb.append("Valid: ").append(valid).append("\n");
        sb.append("Rules Applied: ").append(appliedRules.size()).append("\n");
        sb.append("Rules Failed: ").append(failedRules.size()).append("\n");
        sb.append("Errors: ").append(errors.size()).append("\n");
        sb.append("Warnings: ").append(warnings.size()).append("\n");
        sb.append("Info Messages: ").append(infoMessages.size()).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Merges another validation result into this one.
     *
     * @param other the other validation result
     */
    public void merge(ValidationResult other) {
        if (other == null) return;
        
        synchronized (this) {
            errors.addAll(other.getErrors());
            warnings.addAll(other.getWarnings());
            infoList.addAll(other.getInfos());
            errorMessages.addAll(other.getErrorMessages());
            warningMessages.addAll(other.getWarningMessages());
            infoMessages.addAll(other.getInfoMessages());
            appliedRules.addAll(other.getAppliedRules());
            failedRules.addAll(other.getFailedRules());
            valid = valid && other.isValid();
            
            // Merge metadata
            if (other.getMetadata() != null) {
                if (metadata == null) {
                    metadata = new HashMap<>();
                }
                metadata.putAll(other.getMetadata());
            }
        }
    }
}
