package com.nosql.poc.copy.model;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
public class CopyRequest {
    @NotEmpty
    private String sourceChannelId;
    
    @NotEmpty
    private List<String> targetChannelIds;
    
    @NotEmpty
    private List<String> productIds;
    
    private Map<String, Object> copyOptions;
    
    private boolean validateBeforeCopy = true;
    
    private boolean synchronousCopy = false;
    
    private Map<String, AttributeMapping> attributeMappings;
    
    private Map<String, TransformationRule> transformationRules;
}
