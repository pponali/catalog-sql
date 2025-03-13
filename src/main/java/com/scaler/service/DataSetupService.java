package com.scaler.service;

import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.*;
import com.scaler.validation.factory.ValidationRuleFactory;
import com.scaler.validation.rule.ValidationRules;
import com.scaler.validation.service.CategoryValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for setting up demo data
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DataSetupService {

    private final MerchantRepository merchantRepository;
    private final CategoryRepository categoryRepository;
    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationRulesRepository validationRulesRepository;
    private final ValidationRuleFactory validationRuleFactory;
    private final CategoryValidationService categoryValidationService;
    private final ProductMappingService productMappingService;

    /**
     * Creates validation rules for a category
     * 
     * @param category The category to create validation rules for
     */
    public void createValidationRulesForCategory(Category category) {
        log.info("Creating validation rules for category: {}", category.getName());
        
        // Create validation rules using the factory
        List<ValidationRule> rules = validationRuleFactory.createRulesForCategory(category);
        
        // Save the rules
        for (ValidationRule rule : rules) {
            validationRuleRepository.save(rule);
        }
        
        // Create validation rules using the service
        List<ValidationRules> serviceRules = categoryValidationService.createValidationRulesForCategory(category);
        
        log.info("Created {} validation rules for category: {}", rules.size() + serviceRules.size(), category.getName());
    }

    /**
     * Sets up demo data for the catalog system
     * This method is a placeholder for the actual implementation
     * The actual implementation would be moved from DemoService
     */
    @Transactional
    public void setup() {
        try {
            if (merchantRepository.count() > 0) {
                // Data already exists, skip initialization
                log.info("Demo data already exists, skipping initialization");
                return;
            }
            
            log.info("Setting up demo data...");
            
            // The actual implementation would be moved from DemoService
            // This would include creating merchants, stores, channels, catalogs, etc.
            
            // Create validation rules for categories
            log.info("Creating validation rules for categories...");
            for (Category category : categoryRepository.findAll()) {
                createValidationRulesForCategory(category);
            }
            
            log.info("Demo data setup completed successfully");
        } catch (Exception e) {
            log.error("Failed to initialize demo data", e);
            throw new RuntimeException("Failed to initialize demo data: " + e.getMessage(), e);
        }
    }

    /**
     * Example method demonstrating how to use the validation rules
     * This method would be moved to an appropriate service
     *
     * @param productFeatureValue The product feature value to validate
     * @param validationService The validation service
     * @return List of validation error messages
     */
    public List<String> validateFeatureValueExample(
            ProductFeatureValue productFeatureValue, 
            ValidationService validationService) {
        
        // Get validation rules for the feature from the database
        List<ValidationRules> dbRules = validationRulesRepository.findByTemplateId(
                productFeatureValue.getProductFeature().getTemplate().getId());

        // Convert database rules to validation rule models
        List<ValidationRule> validationRules = new java.util.ArrayList<>();
        for (ValidationRules dbRule : dbRules) {
            validationRules.add(validationService.convertToValidationRule(dbRule));
        }

        // Add a required rule if needed
        if (productFeatureValue.getProductFeature().getTemplate().isMandatory()) {
            validationRules.add(ValidationRule.createRequiredRule(
                    "REQUIRED_" + productFeatureValue.getProductFeature().getCode(),
                    "Required " + productFeatureValue.getProductFeature().getName()
            ));
        }

        // Validate using the rules engine
        return validationService.validateProductFeatureValue(productFeatureValue, validationRules);
    }
}
