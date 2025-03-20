package com.nosql.poc.validation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request model for category validation.
 * Contains all the data needed to validate a category.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryValidationRequest {
    
    /**
     * Unique identifier for the category.
     */
    private String categoryId;
    
    /**
     * Category name.
     */
    private String name;
    
    /**
     * Category code.
     */
    private String code;
    
    /**
     * Category description.
     */
    private String description;
    
    /**
     * Parent category ID (if any).
     */
    private String parentId;
    
    /**
     * Level in the category hierarchy (1 for root, 2 for child of root, etc.).
     */
    private Integer level;
    
    /**
     * Path in the category hierarchy (e.g., "Root > Electronics > Smartphones").
     */
    private String path;
    
    /**
     * List of feature templates associated with this category.
     */
    private List<CategoryFeature> features = new ArrayList<>();
    
    /**
     * Any additional attributes for the category.
     */
    private Map<String, Object> attributes = new HashMap<>();
    
    /**
     * Validation rules to apply.
     * If empty, all applicable rules will be used.
     */
    private List<String> rulesToApply = new ArrayList<>();
    
    /**
     * Flag to enable or disable certain aspects of validation.
     */
    private Map<String, Boolean> validationFlags = new HashMap<>();
    
    /**
     * Add a category feature to this request.
     *
     * @param feature the feature to add
     * @return this request for chaining
     */
    public CategoryValidationRequest addFeature(CategoryFeature feature) {
        if (features == null) {
            features = new ArrayList<>();
        }
        features.add(feature);
        return this;
    }
    
    /**
     * Add an attribute to this request.
     *
     * @param key the attribute key
     * @param value the attribute value
     * @return this request for chaining
     */
    public CategoryValidationRequest addAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
        return this;
    }
    
    /**
     * Set a validation flag.
     *
     * @param flagName the flag name
     * @param value the flag value
     * @return this request for chaining
     */
    public CategoryValidationRequest setValidationFlag(String flagName, Boolean value) {
        if (validationFlags == null) {
            validationFlags = new HashMap<>();
        }
        validationFlags.put(flagName, value);
        return this;
    }
    
    /**
     * Convert this request to a map of key-value pairs.
     *
     * @return a map representation of this request
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", categoryId);
        map.put("name", name);
        map.put("code", code);
        
        if (description != null) {
            map.put("description", description);
        }
        
        if (parentId != null) {
            map.put("parentId", parentId);
        }
        
        if (level != null) {
            map.put("level", level);
        }
        
        if (path != null) {
            map.put("path", path);
        }
        
        // Add all additional attributes
        if (attributes != null) {
            map.putAll(attributes);
        }
        
        return map;
    }
    
    /**
     * Class representing a feature template associated with a category.
     */
    @Data
    @lombok.Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryFeature {
        /**
         * Unique identifier for the feature template.
         */
        private String id;
        
        /**
         * Feature template name.
         */
        private String name;
        
        /**
         * Feature template code.
         */
        private String code;
        
        /**
         * The type of value this feature holds.
         */
        private String valueType;
        
        /**
         * The unit of measurement for this feature (if applicable).
         */
        private String unit;
        
        /**
         * Flag indicating if this feature is required for products in this category.
         */
        private boolean required;
        
        /**
         * Flag indicating if this feature allows multiple values.
         */
        private boolean multiValue;
        
        /**
         * Validation rules for this feature.
         */
        private List<String> validationRules = new ArrayList<>();
        
        /**
         * Add a validation rule to this feature.
         *
         * @param rule the rule to add
         * @return this feature for chaining
         */
        public CategoryFeature addValidationRule(String rule) {
            if (validationRules == null) {
                validationRules = new ArrayList<>();
            }
            validationRules.add(rule);
            return this;
        }
    }
}