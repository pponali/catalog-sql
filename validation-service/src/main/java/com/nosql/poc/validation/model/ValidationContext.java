package com.nosql.poc.validation.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ValidationContext {
    private String validationType; // BASIC, BUSINESS_RULES, CHANNEL_SPECIFIC
    private String channelId;
    private String entityId;
    private Map<String, Object> parameters;
    
    public static ValidationContext basic() {
        return ValidationContext.builder()
            .validationType("BASIC")
            .parameters(new HashMap<>())
            .build();
    }
    
    public static ValidationContext forChannel(String channelId) {
        return ValidationContext.builder()
            .validationType("CHANNEL_SPECIFIC")
            .channelId(channelId)
            .parameters(new HashMap<>())
            .build();
    }
    
    public static ValidationContext forBusinessRules(String entityId) {
        return ValidationContext.builder()
            .validationType("BUSINESS_RULES")
            .entityId(entityId)
            .parameters(new HashMap<>())
            .build();
    }
    
    public ValidationContext addParameter(String key, Object value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(key, value);
        return this;
    }
}
