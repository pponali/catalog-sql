package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Configuration for validation rules in channel integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRules {
    private List<ValidationRule> rules;
    
    private ValidationStrategy strategy;
    
    private Map<String, Object> validationConfig;
}
