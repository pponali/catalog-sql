package com.scaler.validation.validation.factory;

import com.nosql.poc.validation.model.Category;
import com.nosql.poc.validation.model.CategoryFeatureTemplate;
import com.nosql.poc.validation.model.ProductFeature;
import com.nosql.poc.validation.model.SimpleValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Factory for creating validation rules based on product categories
 */
@Component
@RequiredArgsConstructor
public class ValidationRuleFactory {
    
    /**
     * Creates validation rules for a category
     * 
     * @param category The category
     * @return List of validation rules
     */
    public List<SimpleValidationRule> createRulesForCategory(Category category) {
        List<SimpleValidationRule> rules = new ArrayList<>();
        
        // In the MongoDB model, templates are fetched separately
        // Instead, we would typically get them from a repository
        // For now, return an empty list as templates aren't directly on Category
        
        return rules;
    }
    
    /**
     * Creates validation rules for a feature template
     * 
     * @param template The feature template
     * @return List of validation rules
     */
    public List<SimpleValidationRule> createRulesForTemplate(CategoryFeatureTemplate template) {
        List<SimpleValidationRule> rules = new ArrayList<>();
        
        // Create required rule if the feature is required
        if (template.getRequired() != null && template.getRequired()) {
            SimpleValidationRule requiredRule = SimpleValidationRule.createRequiredRule(
                template.getFeatureCode() + "_REQUIRED",
                template.getFeatureName() + " Required"
            );
            rules.add(requiredRule);
        }
        
        // Create pattern rule if validation pattern is specified
        if (template.getValidationPattern() != null && !template.getValidationPattern().isEmpty()) {
            SimpleValidationRule patternRule = SimpleValidationRule.createPatternRule(
                template.getFeatureCode() + "_PATTERN",
                template.getFeatureName() + " Pattern",
                template.getValidationPattern()
            );
            rules.add(patternRule);
        }
        
        // Create range rule if min and max values are specified
        if (template.getMinValue() != null && template.getMaxValue() != null) {
            SimpleValidationRule rangeRule = SimpleValidationRule.createRangeRule(
                template.getFeatureCode() + "_RANGE",
                template.getFeatureName() + " Range",
                String.valueOf(template.getMinValue()),
                String.valueOf(template.getMaxValue())
            );
            rules.add(rangeRule);
        }
        
        // Create length validation rule if min and max length are specified
        if (template.getMinLength() != null && template.getMaxLength() != null) {
            SimpleValidationRule lengthRule = SimpleValidationRule.createLengthRule(
                template.getFeatureCode() + "_LENGTH",
                template.getFeatureName() + " Length",
                template.getMinLength(),
                template.getMaxLength()
            );
            rules.add(lengthRule);
        }
        
        return rules;
    }
    
    /**
     * Creates validation rules for a product feature
     * 
     * @param feature The product feature
     * @param category The category
     * @return List of validation rules
     */
    public List<SimpleValidationRule> createRulesForFeature(ProductFeature feature, Category category) {
        List<SimpleValidationRule> rules = new ArrayList<>();
        
        // In NoSQL model, feature templates are separate and not directly linked
        // We would typically fetch the appropriate template from a repository
        
        // For now, create some basic validation rules based on the feature itself
        
        // Required validation if feature is marked as required
        if (feature.isRequired()) {
            SimpleValidationRule requiredRule = SimpleValidationRule.createRequiredRule(
                feature.getCode() + "_REQUIRED",
                feature.getName() + " Required"
            );
            rules.add(requiredRule);
        }
        
        // Type validation based on the feature's valueType
        if (feature.getValueType() != null && !feature.getValueType().isEmpty()) {
            SimpleValidationRule typeRule = SimpleValidationRule.createTypeRule(
                feature.getCode() + "_TYPE",
                feature.getName() + " Type",
                feature.getValueType()
            );
            rules.add(typeRule);
        }
        
        return rules;
    }
}