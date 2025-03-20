package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Configuration for transformation rules in channel integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformationRules {
    private List<TransformationRule> rules;
    
    private TransformationStrategy strategy;
    
    private Map<String, Object> transformationConfig;
}
