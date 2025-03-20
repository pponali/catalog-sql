package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Strategy for applying transformation rules in channel integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformationStrategy {
    private String strategyType; // SEQUENTIAL, PARALLEL, CONDITIONAL
    
    private List<String> requiredTransformations;
    
    private Map<String, Object> strategyConfig;
    
    private ErrorHandling errorHandling;
}