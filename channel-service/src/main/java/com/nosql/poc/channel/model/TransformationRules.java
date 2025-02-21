package com.nosql.poc.channel.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class TransformationRules {
    private List<TransformationRule> rules;
    
    private TransformationStrategy strategy;
    
    private Map<String, Object> transformationConfig;
}

@Data
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

@Data
public class TransformationStrategy {
    private String strategyType; // SEQUENTIAL, PARALLEL, CONDITIONAL
    
    private List<String> requiredTransformations;
    
    private Map<String, Object> strategyConfig;
    
    private ErrorHandling errorHandling;
}

@Data
public class ErrorHandling {
    private String errorStrategy; // SKIP, RETRY, FALLBACK
    
    private Integer maxRetries;
    
    private String fallbackValue;
    
    private Map<String, Object> errorConfig;
}
