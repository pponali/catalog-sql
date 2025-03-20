package com.scaler.validation.service;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.model.ValidationRule;
import com.scaler.validation.rule.ValidationRules;

import java.util.List;
import java.util.Map;

/**
 * Service interface for category-specific product validation
 */
public interface CategoryValidationService {
    
    /**
     * Validates a product against category-specific rules
     * 
     * @param product The product to validate
     * @param category The category to validate against
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    Map<String, List<String>> validateProduct(Product product, Category category);
    
    /**
     * Validates a product feature against category-specific rules
     * 
     * @param feature The product feature to validate
     * @param category The category to validate against
     * @return List of error messages, empty if validation passes
     */
    List<String> validateProductFeature(ProductFeature feature, Category category);
    
    /**
     * Validates a product feature value against category-specific rules
     * 
     * @param featureValue The product feature value to validate
     * @param feature The product feature
     * @param category The category to validate against
     * @return List of error messages, empty if validation passes
     */
    List<String> validateProductFeatureValue(ProductFeatureValue featureValue, ProductFeature feature, Category category);
    
    /**
     * Gets validation rules for a category
     *
     * @param category The category
     * @return List of validation rules
     */
    List<ValidationRule> getValidationRulesForCategory(Category category);

    /**
     * Gets validation rules for a category and feature
     *
     * @param category The category
     * @param featureCode The feature code
     * @return List of validation rules
     */
    List<ValidationRule> getValidationRulesForCategoryAndFeature(Category category, String featureCode);

    /**
     * Creates validation rules for a category based on its feature templates
     *
     * @param category The category
     * @return List of created validation rules
     */
    List<ValidationRules> createValidationRulesForCategory(Category category);
}
