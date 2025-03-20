package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Represents a mapping between source and target attributes in a channel integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
