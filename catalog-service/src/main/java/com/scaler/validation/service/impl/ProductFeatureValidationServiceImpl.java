package com.scaler.validation.service.impl;

import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.ValidationRuleRepository;
import com.scaler.validation.factory.ValidationRuleFactory;
import com.scaler.validation.service.CategoryValidationService;
import com.scaler.validation.service.ProductFeatureValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Implementation of ProductFeatureValidationService for product feature validation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductFeatureValidationServiceImpl implements ProductFeatureValidationService {

    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationRuleFactory validationRuleFactory;
    private final CategoryValidationService categoryValidationService;
    private final KieContainer kieContainer;

    @Override
    public List<String> validateFeature(ProductFeature feature) {
        List<String> errors = new ArrayList<>();
        
        // Get the template for the feature
        CategoryFeatureTemplate template = feature.getTemplate();
        if (template == null) {
            errors.add("Feature template is missing");
            return errors;
        }
        
        // Get the category for the template
        Category category = template.getCategory();
        if (category == null) {
            errors.add("Category is missing for feature template");
            return errors;
        }
        
        // Validate the feature against the category
        errors.addAll(categoryValidationService.validateProductFeature(feature, category));
        
        return errors;
    }

    @Override
    public List<String> validateFeatureValue(ProductFeatureValue featureValue) {
        List<String> errors = new ArrayList<>();
        
        // Get the feature for the value
        ProductFeature feature = featureValue.getFeature();
        if (feature == null) {
            errors.add("Feature is missing for feature value");
            return errors;
        }
        
        // Get the template for the feature
        CategoryFeatureTemplate template = feature.getTemplate();
        if (template == null) {
            errors.add("Feature template is missing");
            return errors;
        }
        
        // Get the category for the template
        Category category = template.getCategory();
        if (category == null) {
            errors.add("Category is missing for feature template");
            return errors;
        }
        
        // Validate the feature value against the category
        errors.addAll(categoryValidationService.validateProductFeatureValue(featureValue, feature, category));
        
        return errors;
    }

    @Override
    public Map<String, List<String>> validateProductFeatures(Product product) {
        Map<String, List<String>> validationResults = new HashMap<>();
        
        // Get all product categories
        Set<ProductCategory> productCategories = product.getProductCategories();
        if (productCategories == null || productCategories.isEmpty()) {
            return validationResults;
        }
        
        // Validate against each category
        for (ProductCategory productCategory : productCategories) {
            Category category = productCategory.getCategory();
            Map<String, List<String>> categoryResults = validateProductFeaturesForCategory(product, category);
            
            // Merge results
            for (Map.Entry<String, List<String>> entry : categoryResults.entrySet()) {
                validationResults.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
            }
        }
        
        return validationResults;
    }

    @Override
    public Map<String, List<String>> validateProductFeaturesForCategory(Product product, Category category) {
        return categoryValidationService.validateProduct(product, category);
    }
    
    /**
     * Validates a product feature against a set of validation rules
     * 
     * @param feature The product feature to validate
     * @param rules The validation rules to apply
     * @return List of error messages, empty if validation passes
     */
    private List<String> validateFeatureWithRules(ProductFeature feature, List<ValidationRule> rules) {
        List<String> errors = new ArrayList<>();
        
        // Apply each rule
        for (ValidationRule rule : rules) {
            if (!rule.isActive()) {
                continue;
            }
            
            switch (ValidationRule.RuleType.valueOf(rule.getRuleType())) {
                case REQUIRED:
                    if (feature.getValue() == null || feature.getValue().isEmpty()) {
                        errors.add(rule.getName() + ": Value is required");
                    }
                    break;
                case PATTERN:
                    if (feature.getValue() != null && !feature.getValue().matches(rule.getPattern())) {
                        errors.add(rule.getName() + ": Value does not match pattern");
                    }
                    break;
                case RANGE:
                    if (feature.getValue() != null) {
                        try {
                            double value = Double.parseDouble(feature.getValue());
                            Double min = rule.getMinValue();
                            Double max = rule.getMaxValue();
                            
                            if ((min != null && value < min) || (max != null && value > max)) {
                                errors.add(rule.getName() + ": Value must be between " + min + " and " + max);
                            }
                        } catch (NumberFormatException e) {
                            errors.add(rule.getName() + ": Value must be a number");
                        }
                    }
                    break;
                case ALLOWED_VALUES:
                    if (feature.getValue() != null && !rule.getAllowedValues().contains(feature.getValue())) {
                        errors.add(rule.getName() + ": Value must be one of " + String.join(", ", rule.getAllowedValues()));
                    }
                    break;
                case CUSTOM:
                    // Use Drools for custom validation
                    KieSession kieSession = kieContainer.newKieSession();
                    try {
                        List<String> customErrors = new ArrayList<>();
                        kieSession.setGlobal("errors", customErrors);
                        kieSession.insert(feature);
                        kieSession.fireAllRules();
                        
                        errors.addAll(customErrors);
                    } finally {
                        kieSession.dispose();
                    }
                    break;
                default:
                    break;
            }
        }
        
        return errors;
    }
    
    /**
     * Validates a product feature value against a set of validation rules
     * 
     * @param featureValue The product feature value to validate
     * @param rules The validation rules to apply
     * @return List of error messages, empty if validation passes
     */
    private List<String> validateFeatureValueWithRules(ProductFeatureValue featureValue, List<ValidationRule> rules) {
        List<String> errors = new ArrayList<>();
        
        // Apply each rule
        for (ValidationRule rule : rules) {
            if (!rule.isActive()) {
                continue;
            }
            
            String value = featureValue.getValueAsString();
            
            switch (ValidationRule.RuleType.valueOf(rule.getRuleType())) {
                case REQUIRED:
                    if (value == null || value.isEmpty()) {
                        errors.add(rule.getName() + ": Value is required");
                    }
                    break;
                case PATTERN:
                    if (value != null && !value.matches(rule.getPattern())) {
                        errors.add(rule.getName() + ": Value does not match pattern");
                    }
                    break;
                case RANGE:
                    if (value != null) {
                        try {
                            double numValue = Double.parseDouble(value);
                            Double min = rule.getMinValue();
                            Double max = rule.getMaxValue();
                            
                            if ((min != null && numValue < min) || (max != null && numValue > max)) {
                                errors.add(rule.getName() + ": Value must be between " + min + " and " + max);
                            }
                        } catch (NumberFormatException e) {
                            errors.add(rule.getName() + ": Value must be a number");
                        }
                    }
                    break;
                case ALLOWED_VALUES:
                    if (value != null && !rule.getAllowedValues().contains(value)) {
                        errors.add(rule.getName() + ": Value must be one of " + String.join(", ", rule.getAllowedValues()));
                    }
                    break;
                case CUSTOM:
                    // Use Drools for custom validation
                    KieSession kieSession = kieContainer.newKieSession();
                    try {
                        List<String> customErrors = new ArrayList<>();
                        kieSession.setGlobal("errors", customErrors);
                        kieSession.insert(featureValue);
                        kieSession.fireAllRules();
                        
                        errors.addAll(customErrors);
                    } finally {
                        kieSession.dispose();
                    }
                    break;
                default:
                    break;
            }
        }
        
        return errors;
    }
}
