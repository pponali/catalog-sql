package com.scaler.service;

import com.scaler.entity.Product;
import com.scaler.validation.fact.FeatureValidationFact;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CategoryProductValidationService {

    /**
     * Validates a product based on its category-specific validation rules.
     *
     * @param product The product to validate.
     * @return A map of validation results, with feature codes as keys and lists of error messages as values.
     */
    Map<String, List<String>> validateProduct(Product product);

    /**
     * Validates a product feature based on its category-specific validation rules.
     *
     * @param product       The product to validate.
     * @param featureCode   The code of the feature to validate.
     * @param featureValue  The value of the feature to validate.
     * @return A list of error messages for the given feature.
     */
    List<String> validateProductFeature(Product product, String featureCode, String featureValue);

    /**
     * Creates FeatureValidationFact for a product feature.
     *
     * @param product       The product.
     * @param featureCode   The code of the feature.
     * @param featureValue  The value of the feature.
     * @return FeatureValidationFact
     */
    FeatureValidationFact createFeatureValidationFact(Product product, String featureCode, String featureValue);
}
