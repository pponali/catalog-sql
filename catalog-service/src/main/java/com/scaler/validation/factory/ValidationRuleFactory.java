package com.scaler.validation.factory;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.ProductFeature;
import com.scaler.model.ValidationRule;
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
    public List<ValidationRule> createRulesForCategory(Category category) {
        List<ValidationRule> rules = new ArrayList<>();
        
        // Get all feature templates for the category
        Set<CategoryFeatureTemplate> templates = category.getTemplates();
        if (templates == null || templates.isEmpty()) {
            return rules;
        }
        
        // Create validation rules for each template
        for (CategoryFeatureTemplate template : templates) {
            rules.addAll(createRulesForTemplate(template));
        }
        
        return rules;
    }
    
    /**
     * Creates validation rules for a feature template
     * 
     * @param template The feature template
     * @return List of validation rules
     */
    public List<ValidationRule> createRulesForTemplate(CategoryFeatureTemplate template) {
        List<ValidationRule> rules = new ArrayList<>();
        
        // Create required rule if the feature is mandatory
        if (template.isMandatory()) {
            ValidationRule requiredRule = ValidationRule.createRequiredRule(
                template.getCode() + "_REQUIRED",
                template.getName() + " Required"
            );
            rules.add(requiredRule);
        }
        
        // Create pattern rule if validation pattern is specified
        if (template.getValidationPattern() != null && !template.getValidationPattern().isEmpty()) {
            ValidationRule patternRule = ValidationRule.createPatternRule(
                template.getCode() + "_PATTERN",
                template.getName() + " Pattern",
                template.getValidationPattern()
            );
            rules.add(patternRule);
        }
        
        // Create range rule if min and max values are specified
        if (template.getMinValue() != null && template.getMaxValue() != null) {
            ValidationRule rangeRule = ValidationRule.createRangeRule(
                template.getCode() + "_RANGE",
                template.getName() + " Range",
                template.getMinValue(),
                template.getMaxValue()
            );
            rules.add(rangeRule);
        }
        
        // Create allowed values rule if allowed values are specified
        if (template.getAllowedValues() != null && !template.getAllowedValues().isEmpty()) {
            ValidationRule allowedValuesRule = ValidationRule.createAllowedValuesRule(
                template.getCode() + "_ALLOWED_VALUES",
                template.getName() + " Allowed Values",
                template.getAllowedValues()
            );
            rules.add(allowedValuesRule);
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
    public List<ValidationRule> createRulesForFeature(ProductFeature feature, Category category) {
        List<ValidationRule> rules = new ArrayList<>();
        
        // Get the template for the feature
        CategoryFeatureTemplate template = feature.getTemplate();
        if (template == null) {
            return rules;
        }
        
        // Create rules based on the template
        rules.addAll(createRulesForTemplate(template));
        
        return rules;
    }
}
