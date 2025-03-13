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

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final CatalogRepository catalogRepository;
    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationRulesRepository validationRulesRepository;
    private final ValidationRuleFactory validationRuleFactory;
    private final CategoryValidationService categoryValidationService;
    private final ProductMappingService productMappingService;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductFeatureRepository productFeatureRepository;
    private final ProductFeatureMappingRepository productFeatureMappingRepository;

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
            rule.setCreatedBy("system"); // Set created_by field
            validationRuleRepository.save(rule);
        }

        // Create validation rules using the service
        List<ValidationRules> serviceRules = categoryValidationService.createValidationRulesForCategory(category);

        log.info("Created {} validation rules for category: {}", rules.size() + serviceRules.size(), category.getName());
    }

    /**
     * Sets up demo data for the catalog system
     * This method creates sample data for testing validation rules
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

            // Create sample merchants
            Merchant merchant = createSampleMerchant();

            // Create sample catalogs
            Catalog catalog = createSampleCatalog(merchant);

            // Create sample categories with feature templates
            List<Category> categories = createSampleCategories(catalog, merchant);

            // Create validation rules for categories
            log.info("Creating validation rules for categories...");
            for (Category category : categories) {
                createValidationRulesForCategory(category);
            }

            // Create sample products with features
            createSampleProducts(categories, merchant, catalog);

            log.info("Demo data setup completed successfully");
        } catch (Exception e) {
            log.error("Failed to initialize demo data", e);
            throw new RuntimeException("Failed to initialize demo data: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a sample merchant for testing
     *
     * @return The created merchant
     */
    private Merchant createSampleMerchant() {
        log.info("Creating sample merchant");

        Merchant merchant = new Merchant();
        merchant.setCode("SAMPLE_MERCHANT");
        merchant.setName("Sample Merchant");
        merchant.setDescription("A sample merchant for testing");
        merchant.setStatus("ACTIVE");
        merchant.setContactEmail("sample@example.com");
        merchant.setCreatedBy("system");
        merchant.setCreatedDate(LocalDateTime.now());

        return merchantRepository.save(merchant);
    }

    /**
     * Creates a sample catalog for testing
     *
     * @param merchant The merchant to associate with the catalog
     * @return The created catalog
     */
    private Catalog createSampleCatalog(Merchant merchant) {
        log.info("Creating sample catalog");

        Catalog catalog = new Catalog();
        catalog.setCode("SAMPLE_CATALOG");
        catalog.setName("Sample Catalog");
        catalog.setDescription("A sample catalog for testing");
        catalog.setStatus("ACTIVE");
        catalog.setType("PRODUCT");
        catalog.setBusiness(merchant); // Assuming this is the correct method
        catalog.setCreatedBy("system");
        catalog.setCreatedDate(LocalDateTime.now());

        // Save the catalog to the database before returning it
        // This ensures it's not a transient entity when referenced by other entities
        return catalogRepository.save(catalog);
    }

    /**
     * Creates sample categories with feature templates for testing
     *
     * @param catalog  The catalog to associate with the categories
     * @param merchant The merchant to associate with the categories
     * @return The list of created categories
     */
    private List<Category> createSampleCategories(Catalog catalog, Merchant merchant) {
        log.info("Creating sample categories with feature templates");

        List<Category> categories = new ArrayList<>();

        // Create a parent category
        Category electronics = new Category();
        electronics.setCode("ELECTRONICS");
        electronics.setName("Electronics");
        electronics.setDescription("Electronic products");
        electronics.setCatalog(catalog);
        electronics.setMerchant(merchant); // Using setMerchant instead of setBusiness
        electronics.setCreatedBy("system");
        electronics.setCreatedDate(LocalDateTime.now());
        electronics = categoryRepository.save(electronics);
        categories.add(electronics);

        // Create feature templates for electronics
        createFeatureTemplateForCategory(electronics, "BRAND", "Brand", true, null, null, "Samsung,Apple,Sony,LG");
        createFeatureTemplateForCategory(electronics, "MODEL", "Model", true, null, null, null);
        createFeatureTemplateForCategory(electronics, "PRICE", "Price", true, "0", "10000", null);

        // Create a child category
        Category smartphones = new Category();
        smartphones.setCode("SMARTPHONES");
        smartphones.setName("Smartphones");
        smartphones.setDescription("Smartphone products");
        smartphones.setCatalog(catalog);
        smartphones.setMerchant(merchant); // Using setMerchant instead of setBusiness
        smartphones.setParent(electronics);
        smartphones.setCreatedBy("system");
        smartphones.setCreatedDate(LocalDateTime.now());
        smartphones = categoryRepository.save(smartphones);
        categories.add(smartphones);

        // Create feature templates for smartphones
        createFeatureTemplateForCategory(smartphones, "OS", "Operating System", true, null, null, "Android,iOS");
        createFeatureTemplateForCategory(smartphones, "SCREEN_SIZE", "Screen Size", true, "4", "7", null);
        createFeatureTemplateForCategory(smartphones, "CAMERA_MP", "Camera Megapixels", false, "8", "108", null);
        createFeatureTemplateForCategory(smartphones, "STORAGE_GB", "Storage (GB)", true, "16", "1024", "16,32,64,128,256,512,1024");

        return categories;
    }

    /**
     * Creates a feature template for a category
     *
     * @param category      The category to associate with the template
     * @param code          The template code
     * @param name          The template name
     * @param mandatory     Whether the feature is mandatory
     * @param minValue      The minimum value (for range validation)
     * @param maxValue      The maximum value (for range validation)
     * @param allowedValues The allowed values (for enumeration validation)
     * @return The created feature template
     */
    private CategoryFeatureTemplate createFeatureTemplateForCategory(
            Category category, String code, String name, boolean mandatory,
            String minValue, String maxValue, String allowedValues) {

        CategoryFeatureTemplate template = new CategoryFeatureTemplate();
        template.setCode(code);
        template.setName(name);
        template.setMandatory(mandatory);
        template.setCategory(category);
        template.setMinValue(minValue);
        template.setMaxValue(maxValue);
        template.setAllowedValues(allowedValues);
        template.setAttributeType("STRING");
        template.setCreatedBy("system");

        // Add the template to the category
        if (category.getTemplates() == null) {
            category.setTemplates(new java.util.HashSet<>());
        }
        category.getTemplates().add(template);

        return template;
    }

    /**
     * Creates sample products with features for testing
     *
     * @param categories The categories to associate with the products
     * @param merchant   The merchant to associate with the products
     * @param catalog    The catalog to associate with the products
     */
    private void createSampleProducts(List<Category> categories, Merchant merchant, Catalog catalog) {
        log.info("Creating sample products with features");

        // Find the smartphones category
        Category smartphones = categories.stream()
                .filter(c -> c.getCode().equals("SMARTPHONES"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Smartphones category not found"));

        // Create a sample product
        Product iphone = new Product();
        iphone.setCode("IPHONE_13");
        iphone.setName("iPhone 13");
        iphone.setDescription("Apple iPhone 13");
        iphone.setStatus("ACTIVE");
        iphone.setProductType(ProductType.SIMPLE);
        iphone.setMerchant(merchant);
        iphone.setCatalog(catalog);
        iphone.setCreatedBy("system");
        iphone.setCreatedDate(LocalDateTime.now());

        // Save the product
        iphone = productRepository.save(iphone);

        // Associate the product with the smartphones category
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProduct(iphone);
        productCategory.setCategory(smartphones);
        productCategory.setIsPrimary(true);
        productCategory.setMerchant(merchant); // Add merchant to the product category
        productCategory.setCreatedBy("system");
        productCategory.setCreatedDate(LocalDateTime.now());
        productCategoryRepository.save(productCategory);

        // Create features for the product based on the category templates
        for (CategoryFeatureTemplate template : smartphones.getTemplates()) {
            ProductFeature feature = new ProductFeature();
            feature.setCode(template.getCode());
            feature.setName(template.getName());
            feature.setTemplate(template);
            feature.setCreatedBy("system");
            feature.setCreatedDate(LocalDateTime.now());

            // Set feature values based on the template
            switch (template.getCode()) {
                case "BRAND":
                    feature.setDefaultValue("Apple");
                    break;
                case "MODEL":
                    feature.setDefaultValue("iPhone 13");
                    break;
                case "PRICE":
                    feature.setDefaultValue("999");
                    break;
                case "OS":
                    feature.setDefaultValue("iOS");
                    break;
                case "SCREEN_SIZE":
                    feature.setDefaultValue("6.1");
                    break;
                case "CAMERA_MP":
                    feature.setDefaultValue("12");
                    break;
                case "STORAGE_GB":
                    feature.setDefaultValue("128");
                    break;
                default:
                    break;
            }

            // Save the feature
            feature = productFeatureRepository.save(feature);

            // Create a mapping between the product and the feature
            ProductFeatureMapping mapping = new ProductFeatureMapping();
            mapping.setProduct(iphone);
            mapping.setFeature(feature);
            mapping.setCreatedBy("system");
            mapping.setCreatedDate(LocalDateTime.now());
            productFeatureMappingRepository.save(mapping);
        }
    }

    /**
     * Example method demonstrating how to use the validation rules
     * This method would be moved to an appropriate service
     *
     * @param productFeatureValue The product feature value to validate
     * @param validationService   The validation service
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
