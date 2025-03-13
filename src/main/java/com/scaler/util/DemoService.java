package com.scaler.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.ProductFeatureRepository;
import com.scaler.repository.ProductRepository;
import com.scaler.repository.ValidationRuleRepository;
import com.scaler.repository.ValidationRulesRepository;
import com.scaler.service.DataSetupService;
import com.scaler.service.ProductMappingService;
import com.scaler.service.ValidationService;
import com.scaler.validation.factory.ValidationRuleFactory;
import com.scaler.validation.rule.ValidationRules;
import com.scaler.validation.service.CategoryValidationService;
import com.scaler.validation.service.ProductFeatureValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for demo operations
 * This class demonstrates the use of the validation framework for category-specific product validation
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
    private final ValidationRuleFactory validationRuleFactory;

    // Repositories
    private final ProductRepository productRepository;
    private final ProductFeatureRepository productFeatureRepository;
    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationRulesRepository validationRulesRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public DemoService(
            ValidationService validationService,
            DataSetupService dataSetupService,
            ProductMappingService productMappingService,
            CategoryValidationService categoryValidationService,
            ProductFeatureValidationService productFeatureValidationService,
            ValidationRuleFactory validationRuleFactory,
            ProductRepository productRepository,
            ProductFeatureRepository productFeatureRepository,
            ValidationRuleRepository validationRuleRepository,
            ValidationRulesRepository validationRulesRepository) {
        this.validationService = validationService;
        this.dataSetupService = dataSetupService;
        this.productMappingService = productMappingService;
        this.categoryValidationService = categoryValidationService;
        this.productFeatureValidationService = productFeatureValidationService;
        this.validationRuleFactory = validationRuleFactory;
        this.productRepository = productRepository;
        this.productFeatureRepository = productFeatureRepository;
        this.validationRuleRepository = validationRuleRepository;
        this.validationRulesRepository = validationRulesRepository;
        this.objectMapper = new ObjectMapper();
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
        log.info("Validating product: {}", product.getName());
        return productMappingService.validateProduct(product, categoryValidationService);
    }

    /**
     * Validates a product against a specific category's rules
     *
     * @param product    The product to validate
     * @param categoryId The ID of the category to validate against
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    public Map<String, List<String>> validateProductForCategory(Product product, UUID categoryId) {
        log.info("Validating product: {} against category ID: {}", product.getName(), categoryId);

        // Find the category
        Optional<Category> categoryOpt = product.getProductCategories().stream()
                .map(ProductCategory::getCategory)
                .filter(c -> c.getId().equals(categoryId))
                .findFirst();

        if (categoryOpt.isEmpty()) {
            log.warn("Product is not associated with category ID: {}", categoryId);
            return Map.of("error", List.of("Product is not associated with the specified category"));
        }

        // Validate the product against the category
        return categoryValidationService.validateProduct(product, categoryOpt.get());
    }

    /**
     * Validates a product feature
     *
     * @param feature The product feature to validate
     * @return List of error messages, empty if validation passes
     */
    public List<String> validateProductFeature(ProductFeature feature) {
        log.info("Validating product feature: {}", feature.getName());
        return productFeatureValidationService.validateFeature(feature);
    }

    /**
     * Validates a product feature against a specific category's rules
     *
     * @param feature    The product feature to validate
     * @param categoryId The ID of the category to validate against
     * @return List of error messages, empty if validation passes
     */
    public List<String> validateProductFeatureForCategory(ProductFeature feature, UUID categoryId) {
        log.info("Validating product feature: {} against category ID: {}", feature.getName(), categoryId);

        // Find the category
        Category category = feature.getTemplate().getCategory();
        if (!category.getId().equals(categoryId)) {
            log.warn("Feature is not associated with category ID: {}", categoryId);
            return List.of("Feature is not associated with the specified category");
        }

        // Validate the feature against the category
        return categoryValidationService.validateProductFeature(feature, category);
    }

    /**
     * Validates a product feature value
     *
     * @param featureValue The product feature value to validate
     * @return List of error messages, empty if validation passes
     */
    public List<String> validateProductFeatureValue(ProductFeatureValue featureValue) {
        log.info("Validating product feature value: {}", featureValue.getId());
        return productFeatureValidationService.validateFeatureValue(featureValue);
    }

    /**
     * Validates a product feature value against a specific category's rules
     *
     * @param featureValue The product feature value to validate
     * @param categoryId   The ID of the category to validate against
     * @return List of error messages, empty if validation passes
     */
    public List<String> validateProductFeatureValueForCategory(ProductFeatureValue featureValue, UUID categoryId) {
        log.info("Validating product feature value: {} against category ID: {}", featureValue.getId(), categoryId);

        // Find the feature and category
        ProductFeature feature = featureValue.getFeature();
        Category category = feature.getTemplate().getCategory();

        if (!category.getId().equals(categoryId)) {
            log.warn("Feature value is not associated with category ID: {}", categoryId);
            return List.of("Feature value is not associated with the specified category");
        }

        // Validate the feature value against the category
        return categoryValidationService.validateProductFeatureValue(featureValue, feature, category);
    }

    /**
     * Example method demonstrating how to use the validation rules
     *
     * @param productFeatureValue The product feature value to validate
     * @return List of validation error messages
     */
    public List<String> validateFeatureValueExample(ProductFeatureValue productFeatureValue) {
        log.info("Running example validation for feature value: {}", productFeatureValue.getId());
        return dataSetupService.validateFeatureValueExample(productFeatureValue, validationService);
    }

    /**
     * Runs the Drools validation service for testing
     * This method demonstrates how to use the Drools rules engine for validation
     *
     * @param productId The ID of the product to validate
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    public Map<String, List<String>> runDroolsValidation(UUID productId) {
        log.info("Running Drools validation for product ID: {}", productId);

        // Get the product from the repository
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Validate the product using the Drools rules engine
        Map<String, List<String>> validationResults = validateProduct(product);

        // Log the validation results
        if (validationResults.isEmpty()) {
            log.info("Product passed validation: {}", product.getName());
        } else {
            log.warn("Product failed validation: {}", product.getName());
            for (Map.Entry<String, List<String>> entry : validationResults.entrySet()) {
                log.warn("Feature: {}", entry.getKey());
                for (String error : entry.getValue()) {
                    log.warn("  Error: {}", error);
                }
            }
        }

        return validationResults;
    }

    /**
     * Creates a sample product feature value for testing
     * This method demonstrates how to create and validate a product feature value
     *
     * @param featureCode The code of the feature
     * @param value       The value to set
     * @return The validation results
     */
    public List<String> testFeatureValidation(String featureCode, String value) {
        log.info("Testing feature validation for feature code: {} with value: {}", featureCode, value);

        try {
            // Find a product feature with the given code
            List<ProductFeature> features = productFeatureRepository.findAll();
            ProductFeature feature = features.stream()
                    .filter(f -> f.getCode().equals(featureCode))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Feature not found with code: " + featureCode));

            // Create a feature value
            ProductFeatureValue featureValue = new ProductFeatureValue();
            featureValue.setFeature(feature);

            // Create a JsonNode for the attribute value
            JsonNode attributeValue = new TextNode(value);
            featureValue.setAttributeValue(attributeValue);

            featureValue.setCreatedBy("system");
            featureValue.setCreatedDate(java.time.LocalDateTime.now());

            // Validate the feature value
            List<String> validationResults = validateProductFeatureValue(featureValue);

            // Log the validation results
            if (validationResults.isEmpty()) {
                log.info("Feature value passed validation: {}", value);
            } else {
                log.warn("Feature value failed validation: {}", value);
                for (String error : validationResults) {
                    log.warn("  Error: {}", error);
                }
            }

            return validationResults;
        } catch (Exception e) {
            log.error("Error validating feature: {}", e.getMessage(), e);
            return List.of("Error validating feature: " + e.getMessage());
        }
    }

    /**
     * Creates and applies validation rules for a product based on its categories
     * This method demonstrates how to dynamically create and apply validation rules
     *
     * @param productId The ID of the product
     * @return Map of created validation rules by category
     */
    public Map<String, List<ValidationRule>> createAndApplyValidationRules(UUID productId) {
        log.info("Creating and applying validation rules for product ID: {}", productId);

        // Get the product from the repository
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Get all product categories
        Set<ProductCategory> productCategories = product.getProductCategories();
        if (productCategories == null || productCategories.isEmpty()) {
            log.warn("Product has no categories: {}", product.getName());
            return Map.of();
        }

        // Create validation rules for each category
        Map<String, List<ValidationRule>> rulesByCategory = new HashMap<>();
        for (ProductCategory productCategory : productCategories) {
            Category category = productCategory.getCategory();

            // Create validation rules using the factory
            List<ValidationRule> rules = validationRuleFactory.createRulesForCategory(category);

            // Save the rules
            for (ValidationRule rule : rules) {
                rule.setCategoryId(category.getId());
                // No need to set feature code as it's not a direct field
                validationRuleRepository.save(rule);
            }

            // Create validation rules using the service
            List<ValidationRules> serviceRules = categoryValidationService.createValidationRulesForCategory(category);

            // Add the rules to the map
            rulesByCategory.put(category.getName(), rules);

            log.info("Created {} validation rules for category: {}", rules.size() + serviceRules.size(), category.getName());
        }

        return rulesByCategory;
    }

    /**
     * Gets all validation rules for a product based on its categories
     *
     * @param productId The ID of the product
     * @return Map of validation rules by category
     */
    public Map<String, List<ValidationRule>> getValidationRulesForProduct(UUID productId) {
        log.info("Getting validation rules for product ID: {}", productId);

        // Get the product from the repository
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Get all product categories
        Set<ProductCategory> productCategories = product.getProductCategories();
        if (productCategories == null || productCategories.isEmpty()) {
            log.warn("Product has no categories: {}", product.getName());
            return Map.of();
        }

        // Get validation rules for each category
        Map<String, List<ValidationRule>> rulesByCategory = new HashMap<>();
        for (ProductCategory productCategory : productCategories) {
            Category category = productCategory.getCategory();

            // Get validation rules for the category
            List<ValidationRule> rules = categoryValidationService.getValidationRulesForCategory(category);

            // Add the rules to the map
            rulesByCategory.put(category.getName(), rules);

            log.info("Found {} validation rules for category: {}", rules.size(), category.getName());
        }

        return rulesByCategory;
    }

    /**
     * Gets all validation rules for a specific feature of a product
     *
     * @param productId   The ID of the product
     * @param featureCode The code of the feature
     * @return List of validation rules
     */
    public List<ValidationRule> getValidationRulesForProductFeature(UUID productId, String featureCode) {
        log.info("Getting validation rules for product ID: {} and feature code: {}", productId, featureCode);

        // Get the product from the repository
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Get all product categories
        Set<ProductCategory> productCategories = product.getProductCategories();
        if (productCategories == null || productCategories.isEmpty()) {
            log.warn("Product has no categories: {}", product.getName());
            return List.of();
        }

        // Get validation rules for each category and feature
        List<ValidationRule> rules = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            Category category = productCategory.getCategory();

            // Get validation rules for the category and feature
            List<ValidationRule> categoryFeatureRules = categoryValidationService.getValidationRulesForCategoryAndFeature(category, featureCode);
            rules.addAll(categoryFeatureRules);

            log.info("Found {} validation rules for category: {} and feature: {}",
                    categoryFeatureRules.size(), category.getName(), featureCode);
        }

        return rules;
    }
}
