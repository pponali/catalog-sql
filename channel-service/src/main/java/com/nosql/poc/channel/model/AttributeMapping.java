package com.nosql.poc.channel.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AttributeMapping {
    private String sourceAttribute;
    
    private String targetAttribute;
    
    private String mappingType; // DIRECT, TRANSFORM, CUSTOM
    
    private String dataType;
    
    private Boolean required;
    
    private ValidationRule validation;
    
    private TransformationRule transformation;
    
    private List<ValueMapping> valueMappings;
    
    private DefaultValue defaultValue;
    
    private Map<String, Object> mappingRules;
}

@Data
public class ValueMapping {
    private String sourceValue;
    private String targetValue;
    private String mappingLogic;
    private Double confidenceScore;
    private Boolean active;
}

@Data
public class DefaultValue {
    private String value;
    private String condition;
    private String applicability;
    private Map<String, Object> rules;
}
