package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opencsv.exceptions.CsvException;
import com.scaler.builder.ProductBuilder;
import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.*;
import com.scaler.service.DataSetupService;
import com.scaler.service.TestValidationService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for demo operations
 * This class handles data population from CSV files and entity creation using builder classes
 */
@Slf4j
@Service
@Transactional
public class DemoService {

    // Services
    private final DataSetupService dataSetupService;
    private final TestValidationService testValidationService;
    
    // Repositories
    private final ProductRepository productRepository;
    private final CatalogRepository catalogRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final ProductCategoryRepository productCategoryRepository;
    
    // Builders
    private final ProductBuilder productBuilder;
    private final CategoryBuilder categoryBuilder;
    
    // Utilities
    private final CsvDataReaderService csvDataReaderService;

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
            CsvDataReaderService csvDataReaderService) {
        this.dataSetupService = dataSetupService;
        this.testValidationService = testValidationService;
        this.productRepository = productRepository;
        this.catalogRepository = catalogRepository;
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productBuilder = productBuilder;
        this.categoryBuilder = categoryBuilder;
        this.csvDataReaderService = csvDataReaderService;
    }
    
    /**
     * Initialize the demo service and check if data needs to be populated
     * This method is called after the bean is constructed
     */
    @PostConstruct
    public void init() {
        log.info("Initializing DemoService");
        
        // Check if database is empty and needs population
        if (merchantRepository.count() == 0) {
            log.info("Database is empty, will populate data from CSV files when setup() is called");
        } else {
            log.info("Database already contains data, no need to populate from CSV files");
        }
    }

    /**
     * Sets up demo data for the catalog system
     * If the database is empty, populates it from CSV files
     * Otherwise, delegates to DataSetupService for standard setup
     */
    @Transactional
    public void setup() {
        // Check if database is empty
        if (merchantRepository.count() == 0) {
            log.info("Populating database from CSV files");
            try {
                populateDataFromCsvFiles();
            } catch (Exception e) {
                log.error("Error populating data from CSV files", e);
                // Fall back to standard setup if CSV population fails
                dataSetupService.setup();
            }
        } else {
            log.info("Database already contains data, using standard setup");
            dataSetupService.setup();
        }
    }
    
    /**
     * Populates the database with data from CSV files
     * This method reads CSV files and creates entities using builder classes
     * 
     * @throws IOException if there is an error reading the CSV files
     * @throws CsvException if there is an error parsing the CSV files
     * @throws JsonProcessingException if there is an error processing JSON metadata
     */
    private void populateDataFromCsvFiles() throws IOException, CsvException, JsonProcessingException {
        log.info("Populating database from CSV files");
        
        // Step 1: Read merchants from CSV and create merchant entities
        List<Map<String, String>> merchantData = csvDataReaderService.readClassificationAttributes();
        List<Merchant> merchants = createMerchants(merchantData);
        
        // Step 2: Read catalogs from CSV and create catalog entities
        List<Map<String, String>> catalogData = csvDataReaderService.readListOfValues();
        List<Catalog> catalogs = createCatalogs(catalogData, merchants);
        
        // Step 3: Read categories from CSV and create category entities
        List<Map<String, String>> categoryData = csvDataReaderService.readClassificationCategories();
        List<Category> categories = createCategories(categoryData, catalogs, merchants);
        
        // Step 4: Read category hierarchy from CSV and establish parent-child relationships
        List<Map<String, String>> hierarchyData = csvDataReaderService.readPrimaryHierarchy();
        establishCategoryHierarchy(hierarchyData, categories);
        
        // Step 5: Read products from CSV and create product entities
        List<Map<String, String>> productData = csvDataReaderService.readAttributeMappings();
        List<Product> products = createProducts(productData, catalogs, merchants);
        
        // Step 6: Read product-category mappings from CSV and create associations
        createProductCategoryAssociations(productData, products, categories);
        
        log.info("Successfully populated database from CSV files");
        log.info("Created {} merchants, {} catalogs, {} categories, {} products", 
                merchants.size(), catalogs.size(), categories.size(), products.size());
    }
    
    /**
     * Establishes parent-child relationships between categories based on hierarchy data
     *
     * @param hierarchyData List of maps containing hierarchy data from CSV
     * @param categories List of category entities to establish relationships between
     */
    private void establishCategoryHierarchy(List<Map<String, String>> hierarchyData, List<Category> categories) {
        log.info("Establishing category hierarchy from CSV data");
        
        // Create a map of categories by code for quick lookup
        Map<String, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getCode(), category);
        }
        
        for (Map<String, String> data : hierarchyData) {
            // Get category codes for each level
            String level1Code = data.getOrDefault("Category_Level1_Code", "");
            String level2Code = data.getOrDefault("Category_Level2_Code", "");
            String level3Code = data.getOrDefault("Category_Level3_Code", "");
            String level4Code = data.getOrDefault("Category_Level4_Code", "");
            
            // Establish parent-child relationships
            if (!level1Code.isEmpty() && !level2Code.isEmpty()) {
                Category parent = categoryMap.get(level1Code);
                Category child = categoryMap.get(level2Code);
                
                if (parent != null && child != null) {
                    child.setParent(parent);
                    categoryRepository.save(child);
                    log.debug("Set parent-child relationship: {} -> {}", parent.getName(), child.getName());
                }
            }
            
            if (!level2Code.isEmpty() && !level3Code.isEmpty()) {
                Category parent = categoryMap.get(level2Code);
                Category child = categoryMap.get(level3Code);
                
                if (parent != null && child != null) {
                    child.setParent(parent);
                    categoryRepository.save(child);
                    log.debug("Set parent-child relationship: {} -> {}", parent.getName(), child.getName());
                }
            }
            
            if (!level3Code.isEmpty() && !level4Code.isEmpty()) {
                Category parent = categoryMap.get(level3Code);
                Category child = categoryMap.get(level4Code);
                
                if (parent != null && child != null) {
                    child.setParent(parent);
                    categoryRepository.save(child);
                    log.debug("Set parent-child relationship: {} -> {}", parent.getName(), child.getName());
                }
            }
        }
    }
    
    /**
     * Creates product entities from CSV data using the ProductBuilder
     *
     * @param productData List of maps containing product data from CSV
     * @param catalogs List of catalog entities to associate with products
     * @param merchants List of merchant entities to associate with products
     * @return List of created product entities
     * @throws JsonProcessingException if there is an error processing JSON metadata
     */
    private List<Product> createProducts(List<Map<String, String>> productData, 
                                        List<Catalog> catalogs, 
                                        List<Merchant> merchants) throws JsonProcessingException {
        log.info("Creating products from CSV data");
        
        List<Product> products = new ArrayList<>();
        
        // Get the first catalog and merchant to associate with products
        Catalog defaultCatalog = catalogs.isEmpty() ? null : catalogs.get(0);
        Merchant defaultMerchant = merchants.isEmpty() ? null : merchants.get(0);
        
        for (Map<String, String> data : productData) {
            // Use the ProductBuilder to create a product with metadata
            Product product = productBuilder.createProductWithMetadata(data);
            
            // Associate with catalog and merchant if available
            if (defaultCatalog != null) {
                product.setCatalog(defaultCatalog);
            }
            
            if (defaultMerchant != null) {
                product.setMerchant(defaultMerchant);
            }
            
            product = productRepository.save(product);
            products.add(product);
            
            log.debug("Created product: {}", product.getName());
        }
        
        return products;
    }
    
    /**
     * Creates product-category associations from CSV data
     *
     * @param productData List of maps containing product data from CSV
     * @param products List of product entities to associate with categories
     * @param categories List of category entities to associate with products
     */
    private void createProductCategoryAssociations(List<Map<String, String>> productData, 
                                                 List<Product> products, 
                                                 List<Category> categories) {
        log.info("Creating product-category associations from CSV data");
        
        // Create maps for quick lookup
        Map<String, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getCode(), product);
        }
        
        Map<String, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getCode(), category);
        }
        
        for (Map<String, String> data : productData) {
            String productCode = data.getOrDefault("Product_Code", "");
            String categoryCode = data.getOrDefault("Category_Code", "");
            
            if (!productCode.isEmpty() && !categoryCode.isEmpty()) {
                Product product = productMap.get(productCode);
                Category category = categoryMap.get(categoryCode);
                
                if (product != null && category != null) {
                    // Create product-category association
                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setId(UUID.randomUUID());
                    productCategory.setProduct(product);
                    productCategory.setCategory(category);
                    productCategory.setIsPrimary(true);
                    productCategory.setCreatedBy("system");
                    productCategory.setCreatedDate(LocalDateTime.now());
                    
                    // Set merchant if available
                    if (product.getMerchant() != null) {
                        productCategory.setMerchant(product.getMerchant());
                    }
                    
                    productCategoryRepository.save(productCategory);
                    log.debug("Created product-category association: {} -> {}", product.getName(), category.getName());
                }
            }
        }
    }

    /**
     * Creates merchant entities from CSV data
     *
     * @param merchantData List of maps containing merchant data from CSV
     * @return List of created merchant entities
     */
    private List<Merchant> createMerchants(List<Map<String, String>> merchantData) {
        log.info("Creating {} merchants from CSV data", merchantData.size());
        
        List<Merchant> merchants = new ArrayList<>();
        
        for (Map<String, String> data : merchantData) {
            String code = data.getOrDefault("Merchant_Code", "MERCH-" + UUID.randomUUID().toString().substring(0, 8));
            String name = data.getOrDefault("Merchant_Name", "Merchant " + UUID.randomUUID().toString().substring(0, 8));
            
            Merchant merchant = new Merchant();
            merchant.setId(UUID.randomUUID());
            merchant.setCode(code);
            merchant.setName(name);
            merchant.setCreatedBy("system");
            merchant.setCreatedDate(LocalDateTime.now());
            
            merchant = merchantRepository.save(merchant);
            merchants.add(merchant);
            
            log.debug("Created merchant: {}", merchant.getName());
        }
        
        // If no merchants were created, create a default one
        if (merchants.isEmpty()) {
            Merchant defaultMerchant = new Merchant();
            defaultMerchant.setId(UUID.randomUUID());
            defaultMerchant.setCode("DEFAULT_MERCHANT");
            defaultMerchant.setName("Default Merchant");
            defaultMerchant.setCreatedBy("system");
            defaultMerchant.setCreatedDate(LocalDateTime.now());
            
            defaultMerchant = merchantRepository.save(defaultMerchant);
            merchants.add(defaultMerchant);
            
            log.debug("Created default merchant: {}", defaultMerchant.getName());
        }
        
        return merchants;
    }
    
    /**
     * Creates catalog entities from CSV data
     *
     * @param catalogData List of maps containing catalog data from CSV
     * @param merchants List of merchant entities to associate with catalogs
     * @return List of created catalog entities
     */
    private List<Catalog> createCatalogs(List<Map<String, String>> catalogData, List<Merchant> merchants) {
        log.info("Creating catalogs from CSV data");
        
        List<Catalog> catalogs = new ArrayList<>();
        
        // Get the first merchant to associate with catalogs
        Merchant defaultMerchant = merchants.isEmpty() ? 
                null : merchants.get(0);
        
        for (Map<String, String> data : catalogData) {
            String code = data.getOrDefault("Catalog_Code", "CAT-" + UUID.randomUUID().toString().substring(0, 8));
            String name = data.getOrDefault("Catalog_Name", "Catalog " + UUID.randomUUID().toString().substring(0, 8));
            
            Catalog catalog = new Catalog();
            catalog.setId(UUID.randomUUID());
            catalog.setCode(code);
            catalog.setName(name);
            catalog.setCreatedBy("system");
            catalog.setCreatedDate(LocalDateTime.now());
            
            // Associate with merchant if available
            if (defaultMerchant != null) {
                catalog.setMerchantId(defaultMerchant.getId());
            }
            
            catalog = catalogRepository.save(catalog);
            catalogs.add(catalog);
            
            log.debug("Created catalog: {}", catalog.getName());
        }
        
        // If no catalogs were created, create a default one
        if (catalogs.isEmpty() && defaultMerchant != null) {
            Catalog defaultCatalog = new Catalog();
            defaultCatalog.setId(UUID.randomUUID());
            defaultCatalog.setCode("DEFAULT_CATALOG");
            defaultCatalog.setName("Default Catalog");
            defaultCatalog.setMerchantId(defaultMerchant.getId());
            defaultCatalog.setCreatedBy("system");
            defaultCatalog.setCreatedDate(LocalDateTime.now());
            
            defaultCatalog = catalogRepository.save(defaultCatalog);
            catalogs.add(defaultCatalog);
            
            log.debug("Created default catalog: {}", defaultCatalog.getName());
        }
        
        return catalogs;
    }
    
    /**
     * Creates category entities from CSV data using the CategoryBuilder
     *
     * @param categoryData List of maps containing category data from CSV
     * @param catalogs List of catalog entities to associate with categories
     * @param merchants List of merchant entities to associate with categories
     * @return List of created category entities
     * @throws JsonProcessingException if there is an error processing JSON metadata
     */
    private List<Category> createCategories(List<Map<String, String>> categoryData, 
                                           List<Catalog> catalogs, 
                                           List<Merchant> merchants) throws JsonProcessingException {
        log.info("Creating categories from CSV data");
        
        List<Category> categories = new ArrayList<>();
        
        // Get the first catalog and merchant to associate with categories
        Catalog defaultCatalog = catalogs.isEmpty() ? null : catalogs.get(0);
        Merchant defaultMerchant = merchants.isEmpty() ? null : merchants.get(0);
        
        for (Map<String, String> data : categoryData) {
            // Use the CategoryBuilder to create a category with metadata
            Category category = categoryBuilder.createCategoryWithMetadata(data);
            
            // Associate with catalog and merchant if available
            if (defaultCatalog != null) {
                category.setCatalog(defaultCatalog);
            }
            
            if (defaultMerchant != null) {
                category.setMerchant(defaultMerchant);
            }
            
            category = categoryRepository.save(category);
            categories.add(category);
            
            log.debug("Created category: {}", category.getName());
        }
        
        return categories;
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
