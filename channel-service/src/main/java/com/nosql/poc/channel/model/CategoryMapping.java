package com.nosql.poc.channel.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CategoryMapping {
    private String sourceCategoryId;
    
    private String sourceCategoryName;
    
    private String targetCategoryId;
    
    private String targetCategoryName;
    
    private String mappingType; // EXACT, SIMILAR, CUSTOM
    
    private Double confidenceScore;
    
    private Boolean active;
    
    private List<AttributeMapping> categoryAttributes;
    
    private ValidationRules categoryValidations;
    
    private Map<String, Object> mappingRules;
    
    private List<String> allowedProductTypes;
    
    private Map<String, Object> categoryMetadata;
}
