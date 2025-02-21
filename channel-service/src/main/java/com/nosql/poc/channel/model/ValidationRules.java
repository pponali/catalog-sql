package com.nosql.poc.channel.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ValidationRules {
    private List<ValidationRule> rules;
    
    private ValidationStrategy strategy;
    
    private Map<String, Object> validationConfig;
}

@Data
public class ValidationRule {
    private String ruleId;
    
    private String ruleType; // REQUIRED, FORMAT, RANGE, ENUM, CUSTOM
    
    private String field;
    
    private String condition;
    
    private String severity; // ERROR, WARNING, INFO
    
    private String message;
    
    private Map<String, Object> parameters;
    
    private List<String> dependentFields;
    
    private Boolean active;
}

@Data
public class ValidationStrategy {
    private String strategyType; // ALL, ANY, CUSTOM
    
    private Integer minPassingRules;
    
    private List<String> criticalRules;
    
    private Map<String, Object> strategyConfig;
}
