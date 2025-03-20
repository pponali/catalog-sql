package com.nosql.poc.validation.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Context for validation operations, including the entity to validate and applicable rules.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationContext {
    private String entityType; // PRODUCT, CATEGORY, PRODUCT_FEATURE, etc.
    private JsonNode entity;
    private List<SimpleValidationRule> rules;
    // Enhanced validation rules
    private List<EnhancedValidationRule> enhancedRules;
    private String validationType; // BASIC, BUSINESS_RULES, CHANNEL_SPECIFIC, ENHANCED
    private String channelId;
    private String entityId;
    private Map<String, Object> parameters;
    // Cache for calculated values
    private Map<String, Object> calculatedValues;
    
    /**
     * Creates a basic validation context.
     *
     * @return a basic validation context
     */
    public static ValidationContext basic() {
        return ValidationContext.builder()
            .validationType("BASIC")
            .parameters(new HashMap<>())
            .build();
    }
    
    /**
     * Creates a channel-specific validation context.
     *
     * @param channelId the channel ID
     * @return a channel-specific validation context
     */
    public static ValidationContext forChannel(String channelId) {
        return ValidationContext.builder()
            .validationType("CHANNEL_SPECIFIC")
            .channelId(channelId)
            .parameters(new HashMap<>())
            .build();
    }
    
    /**
     * Creates a business rules validation context.
     *
     * @param entityId the entity ID
     * @return a business rules validation context
     */
    public static ValidationContext forBusinessRules(String entityId) {
        return ValidationContext.builder()
            .validationType("BUSINESS_RULES")
            .entityId(entityId)
            .parameters(new HashMap<>())
            .build();
    }
    
    /**
     * Creates an enhanced validation context.
     *
     * @param entityId the entity ID
     * @return an enhanced validation context
     */
    public static ValidationContext forEnhancedRules(String entityId) {
        return ValidationContext.builder()
            .validationType("ENHANCED")
            .entityId(entityId)
            .parameters(new HashMap<>())
            .calculatedValues(new HashMap<>())
            .build();
    }
    
    /**
     * Adds a parameter to the context.
     *
     * @param key the parameter key
     * @param value the parameter value
     * @return this context
     */
    public ValidationContext addParameter(String key, Object value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(key, value);
        return this;
    }
    
    /**
     * Adds a calculated value to the context.
     *
     * @param key the key
     * @param value the calculated value
     * @return this context
     */
    public ValidationContext addCalculatedValue(String key, Object value) {
        if (calculatedValues == null) {
            calculatedValues = new HashMap<>();
        }
        calculatedValues.put(key, value);
        return this;
    }
    
    /**
     * Gets a calculated value from the context.
     *
     * @param key the key
     * @return the calculated value, or null if not found
     */
    public Object getCalculatedValue(String key) {
        if (calculatedValues == null) {
            return null;
        }
        return calculatedValues.get(key);
    }
    
    /**
     * Adds a simple validation rule to the context.
     *
     * @param rule the rule to add
     * @return this context
     */
    public ValidationContext addRule(SimpleValidationRule rule) {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        rules.add(rule);
        return this;
    }
    
    /**
     * Adds an enhanced validation rule to the context.
     *
     * @param rule the rule to add
     * @return this context
     */
    public ValidationContext addEnhancedRule(EnhancedValidationRule rule) {
        if (enhancedRules == null) {
            enhancedRules = new ArrayList<>();
        }
        enhancedRules.add(rule);
        return this;
    }
}
