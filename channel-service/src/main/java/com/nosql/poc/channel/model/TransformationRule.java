package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Rule for transforming data in channel integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformationRule {
    private String ruleId;
    
    private String ruleType; // FORMAT, CONCAT, SPLIT, REPLACE, CUSTOM
    
    private String field;
    
    private String transformation;
    
    private Map<String, Object> parameters;
    
    private List<String> dependentFields;
    
    private Boolean active;
    
    private Integer executionOrder;
}