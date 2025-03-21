package com.scaler.validation.validation.service;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;

import java.util.List;
import java.util.Map;

/**
 * Service interface for product feature validation
 */
public interface ProductFeatureValidationService {
    
    /**
     * Validates a product feature
     * 
     * @param feature The product feature to validate
     * @return List of error messages, empty if validation passes
     */
    List<String> validateFeature(ProductFeature feature);
    
    /**
     * Validates a product feature value
     * 
     * @param featureValue The product feature value to validate
     * @return List of error messages, empty if validation passes
     */
    List<String> validateFeatureValue(ProductFeatureValue featureValue);
    
    /**
     * Validates all features of a product
     * 
     * @param product The product to validate
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    Map<String, List<String>> validateProductFeatures(Product product);
    
    /**
     * Validates all features of a product against a specific category
     * 
     * @param product The product to validate
     * @param category The category to validate against
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    Map<String, List<String>> validateProductFeaturesForCategory(Product product, Category category);
}
