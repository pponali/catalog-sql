package com.scaler.util;

import com.scaler.builder.*;
import com.scaler.model.*;
import com.scaler.repository.*;
import com.scaler.service.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service for demo operations
 * This class has been refactored to use the ResilientCsvDataLoader
 */
@Slf4j
@Service
@Transactional
public class DemoService {

    // Services
    private final DataSetupService dataSetupService;
    private final TestValidationService testValidationService;
    private final ResilientDataSetupService resilientDataSetupService;
    

    private final MerchantRepository merchantRepository;

    private final LoadDataFromCsvService loadDataFromCsvService;
    
    @Value("${app.csv.auto.populate:false}")
    private boolean autoPopulateData;

    @Value("${app.resilient.csv.auto.populate:false}")
    private boolean resilientCsvData;

    @Autowired
    public DemoService(
            DataSetupService dataSetupService,
            TestValidationService testValidationService,
            ProductRepository productRepository,
            CatalogRepository catalogRepository,
            CategoryRepository categoryRepository,
            MerchantRepository merchantRepository,
            ProductCategoryRepository productCategoryRepository,
            ProductBuilder productBuilder,
            CategoryBuilder categoryBuilder,
            CsvDataReaderService csvDataReaderService,
            ResilientCsvDataLoader resilientCsvDataLoader,
            LoadDataFromCsvService loadDataFromCsvService,
            ProductFeatureRepository productFeatureRepository, ResilientDataSetupService resilientDataSetupService) {
        this.dataSetupService = dataSetupService;
        this.testValidationService = testValidationService;
        this.merchantRepository = merchantRepository;
        this.loadDataFromCsvService = loadDataFromCsvService;
        this.resilientDataSetupService = resilientDataSetupService;
    }
    
    /**
     * Initialize the demo service and check if data needs to be populated
     * This method is called after the bean is constructed
     */
    @PostConstruct
    public void init() {
        log.info("Initializing DemoService with resilient CSV data loading capabilities");
        
        // Check if database is empty and auto-populate is enabled
        if(resilientCsvData){
            resilientDataSetupService.populateDataFromCsvFiles();
            log.info("Data is populating from resilient service.");
        }
        if (autoPopulateData && merchantRepository.count() == 0) {
            log.info("Auto-populate is enabled and database is empty, will populate data from CSV files");
            try {
                loadDataFromCsvService.populateDataFromCsvFiles();
            } catch (Exception e) {
                log.error("Failed to auto-populate data from CSV files", e);
                // Continue with application startup even if data population fails
            }
        } else {
            if (!autoPopulateData) {
                loadDataFromCsvService.populateDataFromCsvFiles();
                log.info("Auto-populate is disabled, skipping data population");
            } else {
                log.info("Database already contains data, skipping data population");
            }
        }
    }

    /**
     * Sets up demo data for the catalog system
     * If the database is empty, populates it from CSV files
     * Otherwise, delegates to DataSetupService for standard setup
     */
    @Transactional
    public void setup() {
        if(resilientCsvData){
            resilientDataSetupService.populateDataFromCsvFiles();
        }
        // Check if database is empty
        if (merchantRepository.count() == 0) {
            log.info("Populating database from CSV files with resilient loading");
            try {
                dataSetupService.setup();
            } catch (Exception e) {
                log.error("Failed to populate data from CSV files, falling back to standard setup", e);
                dataSetupService.setup();
            }
        } else {
            log.info("Database already contains data, using standard setup");
            dataSetupService.setup();
        }
    }
    

    /**
     * Runs the Drools validation service for testing by delegating to TestValidationService
     * This method demonstrates how to use the Drools rules engine for validation
     *
     * @param productId The ID of the product to validate
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    public Map<String, List<String>> runDroolsValidation(UUID productId) {
        log.info("Delegating Drools validation for product ID: {} to TestValidationService", productId);
        return testValidationService.runDroolsValidation(productId);
    }

    /**
     * Creates a sample product feature value for testing by delegating to TestValidationService
     * This method demonstrates how to create and validate a product feature value
     *
     * @param featureCode The code of the feature
     * @param value       The value to set
     * @return The validation results
     */
    public List<String> testFeatureValidation(String featureCode, String value) {
        log.info("Delegating feature validation for feature code: {} with value: {} to TestValidationService", featureCode, value);
        return testValidationService.testFeatureValidation(featureCode, value);
    }

    /**
     * Creates and applies validation rules for a product based on its categories
     * by delegating to TestValidationService
     *
     * @param productId The ID of the product
     * @return Map of created validation rules by category
     */
    public Map<String, List<ValidationRule>> createAndApplyValidationRules(UUID productId) {
        log.info("Delegating creation and application of validation rules for product ID: {} to TestValidationService", productId);
        return testValidationService.createAndApplyValidationRules(productId);
    }

    /**
     * Gets all validation rules for a product based on its categories
     * by delegating to TestValidationService
     *
     * @param productId The ID of the product
     * @return Map of validation rules by category
     */
    public Map<String, List<ValidationRule>> getValidationRulesForProduct(UUID productId) {
        log.info("Delegating retrieval of validation rules for product ID: {} to TestValidationService", productId);
        return testValidationService.getValidationRulesForProduct(productId);
    }

    /**
     * Gets all validation rules for a specific feature of a product
     * by delegating to TestValidationService
     *
     * @param productId   The ID of the product
     * @param featureCode The code of the feature
     * @return List of validation rules
     */
    public List<ValidationRule> getValidationRulesForProductFeature(UUID productId, String featureCode) {
        log.info("Delegating retrieval of validation rules for product ID: {} and feature code: {} to TestValidationService", productId, featureCode);
        return testValidationService.getValidationRulesForProductFeature(productId, featureCode);
    }
}
