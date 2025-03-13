package com.scaler.util;

import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.service.DataSetupService;
import com.scaler.service.ProductMappingService;
import com.scaler.service.ValidationService;
import com.scaler.validation.service.CategoryValidationService;
import com.scaler.validation.service.ProductFeatureValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service for demo operations
 * This class has been refactored to delegate to specialized services
 */
@Slf4j
@Service
@Transactional
public class DemoService {
    
    // Services
    private final ValidationService validationService;
    private final DataSetupService dataSetupService;
    private final ProductMappingService productMappingService;
    private final CategoryValidationService categoryValidationService;
    private final ProductFeatureValidationService productFeatureValidationService;
    
    @Autowired
    public DemoService(
            ValidationService validationService,
            DataSetupService dataSetupService,
            ProductMappingService productMappingService,
            CategoryValidationService categoryValidationService,
            ProductFeatureValidationService productFeatureValidationService) {
        this.validationService = validationService;
        this.dataSetupService = dataSetupService;
        this.productMappingService = productMappingService;
        this.categoryValidationService = categoryValidationService;
        this.productFeatureValidationService = productFeatureValidationService;
    }

    /**
     * Sets up demo data for the catalog system
     * Delegates to DataSetupService
     */
    @Transactional
    public void setup() {
        dataSetupService.setup();
    }
    
    /**
     * Validates a product against category-specific rules
     * 
     * @param product The product to validate
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    public Map<String, List<String>> validateProduct(Product product) {
        return productMappingService.validateProduct(product, categoryValidationService);
    }
    
    /**
     * Validates a product feature
     * 
     * @param feature The product feature to validate
     * @return List of error messages, empty if validation passes
     */
    public List<String> validateProductFeature(ProductFeature feature) {
        return productFeatureValidationService.validateFeature(feature);
    }
    
    /**
     * Validates a product feature value
     * 
     * @param featureValue The product feature value to validate
     * @return List of error messages, empty if validation passes
     */
    public List<String> validateProductFeatureValue(ProductFeatureValue featureValue) {
        return productFeatureValidationService.validateFeatureValue(featureValue);
    }
    
    /**
     * Example method demonstrating how to use the validation rules
     *
     * @param productFeatureValue The product feature value to validate
     * @return List of validation error messages
     */
    public List<String> validateFeatureValueExample(ProductFeatureValue productFeatureValue) {
        return dataSetupService.validateFeatureValueExample(productFeatureValue, validationService);
    }
}
