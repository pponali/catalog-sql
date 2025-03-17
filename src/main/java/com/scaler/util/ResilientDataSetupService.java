package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scaler.builder.ProductBuilder;
import com.scaler.entity.*;
import com.scaler.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
public class ResilientDataSetupService {


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
    private final ResilientCsvDataLoader resilientCsvDataLoader;

    public ResilientDataSetupService(ProductRepository productRepository, CatalogRepository catalogRepository, CategoryRepository categoryRepository, MerchantRepository merchantRepository, ProductCategoryRepository productCategoryRepository, ProductBuilder productBuilder, CategoryBuilder categoryBuilder, CsvDataReaderService csvDataReaderService, ResilientCsvDataLoader resilientCsvDataLoader) {
        this.productRepository = productRepository;
        this.catalogRepository = catalogRepository;
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productBuilder = productBuilder;
        this.categoryBuilder = categoryBuilder;
        this.csvDataReaderService = csvDataReaderService;
        this.resilientCsvDataLoader = resilientCsvDataLoader;
    }


    /**
     * Populates the database with data from CSV files using resilient loading
     * This method uses the ResilientCsvDataLoader to load data from CSV files
     */
    @Transactional
    public void populateDataFromCsvFiles() {
        log.info("Populating database from CSV files using resilient loading");

        try {
            // Step 1: Read merchants from CSV and create merchant entities
            List<Map<String, String>> merchantData = resilientCsvDataLoader.loadCsvData("csv/merchants.csv");
            if (merchantData.isEmpty()) {
                merchantData = resilientCsvDataLoader.loadCsvData("csv/classification_attributes.csv");
            }
            List<Merchant> merchants = createMerchants(merchantData);

            // Step 2: Read catalogs from CSV and create catalog entities
            List<Map<String, String>> catalogData = resilientCsvDataLoader.loadCsvData("csv/catalogs.csv");
            if (catalogData.isEmpty()) {
                catalogData = resilientCsvDataLoader.loadCsvData("csv/list_of_values.csv");
            }
            List<Catalog> catalogs = createCatalogs(catalogData, merchants);

            // Step 3: Read categories from CSV and create category entities
            List<Map<String, String>> categoryData = resilientCsvDataLoader.loadCsvData("csv/categories.csv");
            if (categoryData.isEmpty()) {
                categoryData = resilientCsvDataLoader.loadCsvData("csv/classification_category.csv");
            }
            List<Category> categories = createCategories(categoryData, catalogs, merchants);

            // Step 4: Read category hierarchy from CSV and establish parent-child relationships
            List<Map<String, String>> hierarchyData = resilientCsvDataLoader.loadCsvData("csv/category_hierarchy.csv");
            if (hierarchyData.isEmpty()) {
                hierarchyData = resilientCsvDataLoader.loadCsvData("csv/primary_hierarchy.csv");
            }
            establishCategoryHierarchy(hierarchyData, categories);

            // Step 5: Read products from CSV and create product entities
            List<Map<String, String>> productData = resilientCsvDataLoader.loadCsvData("csv/sample_products.csv");
            if (productData.isEmpty()) {
                productData = resilientCsvDataLoader.loadCsvData("csv/classification_attr_mapping.csv");
            }
            List<Product> products = createProducts(productData, catalogs, merchants);

            // Step 6: Read product-category mappings from CSV and create associations
            List<Map<String, String>> mappingData = resilientCsvDataLoader.loadCsvData("csv/product_category_mappings.csv");
            if (mappingData.isEmpty()) {
                mappingData = productData; // Use product data for mappings if no dedicated mapping file
            }
            createProductCategoryAssociations(mappingData, products, categories);

            log.info("Successfully populated database from CSV files");
            log.info("Created {} merchants, {} catalogs, {} categories, {} products",
                    merchants.size(), catalogs.size(), categories.size(), products.size());
        } catch (Exception e) {
            log.error("Failed to populate database from CSV files", e);
            throw new RuntimeException("Failed to populate database from CSV files", e);
        }
    }

    /**
     * Establishes parent-child relationships between categories based on hierarchy data
     *
     * @param hierarchyData List of maps containing hierarchy data from CSV
     * @param categories List of category entities to establish relationships between
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    private void establishCategoryHierarchy(List<Map<String, String>> hierarchyData, List<Category> categories) {
        log.info("Establishing category hierarchy from CSV data");

        // Create a map of categories by code for quick lookup
        Map<String, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getCode(), category);
        }

        for (Map<String, String> data : hierarchyData) {
            try {
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
            } catch (Exception e) {
                log.error("Error establishing category hierarchy from data: {}", data, e);
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
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    private List<Product> createProducts(List<Map<String, String>> productData,
                                         List<Catalog> catalogs,
                                         List<Merchant> merchants) throws JsonProcessingException {
        log.info("Creating products from CSV data");

        List<Product> products = new ArrayList<>();

        // Get the first catalog and merchant to associate with products
        Catalog defaultCatalog = catalogs.isEmpty() ? null : catalogs.get(0);
        Merchant defaultMerchant = merchants.isEmpty() ? null : merchants.get(0);

        for (Map<String, String> data : productData) {
            try {
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
            } catch (Exception e) {
                log.error("Error creating product from data: {}", data, e);
            }
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
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
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
            try {
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
            } catch (Exception e) {
                log.error("Error creating product-category association from data: {}", data, e);
            }
        }
    }

    /**
     * Creates merchant entities from CSV data
     *
     * @param merchantData List of maps containing merchant data from CSV
     * @return List of created merchant entities
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    private List<Merchant> createMerchants(List<Map<String, String>> merchantData) {
        log.info("Creating {} merchants from CSV data", merchantData.size());

        List<Merchant> merchants = new ArrayList<>();

        for (Map<String, String> data : merchantData) {
            try {
                String code = data.getOrDefault("Merchant_Code", "MERCH-" + UUID.randomUUID().toString().substring(0, 8));
                String name = data.getOrDefault("Merchant_Name", "Merchant " + UUID.randomUUID().toString().substring(0, 8));

                // Check if merchant already exists
                Optional<Merchant> existingMerchant = merchantRepository.findByCode(code);
                if (existingMerchant.isPresent()) {
                    log.debug("Merchant with code {} already exists, skipping creation", code);
                    merchants.add(existingMerchant.get());
                    continue;
                }

                Merchant merchant = new Merchant();
                merchant.setId(UUID.randomUUID());
                merchant.setCode(code);
                merchant.setName(name);
                merchant.setCreatedBy("system");
                merchant.setCreatedDate(LocalDateTime.now());

                merchant = merchantRepository.save(merchant);
                merchants.add(merchant);

                log.debug("Created merchant: {}", merchant.getName());
            } catch (Exception e) {
                log.error("Error creating merchant from data: {}", data, e);
            }
        }

        // If no merchants were created, create a default one
        if (merchants.isEmpty()) {
            String defaultCode = "DEFAULT_MERCHANT";

            // Check if default merchant already exists
            Optional<Merchant> existingDefaultMerchant = merchantRepository.findByCode(defaultCode);
            if (existingDefaultMerchant.isPresent()) {
                log.debug("Default merchant already exists, using existing one");
                merchants.add(existingDefaultMerchant.get());
                return merchants;
            }

            Merchant defaultMerchant = new Merchant();
            defaultMerchant.setId(UUID.randomUUID());
            defaultMerchant.setCode(defaultCode);
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
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    private List<Catalog> createCatalogs(List<Map<String, String>> catalogData, List<Merchant> merchants) {
        log.info("Creating catalogs from CSV data");

        List<Catalog> catalogs = new ArrayList<>();

        // Get the first merchant to associate with catalogs
        Merchant defaultMerchant = merchants.isEmpty() ?
                null : merchants.get(0);

        for (Map<String, String> data : catalogData) {
            try {
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
            } catch (Exception e) {
                log.error("Error creating catalog from data: {}", data, e);
            }
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
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    private List<Category> createCategories(List<Map<String, String>> categoryData,
                                            List<Catalog> catalogs,
                                            List<Merchant> merchants) throws JsonProcessingException {
        log.info("Creating categories from CSV data");

        List<Category> categories = new ArrayList<>();

        // Get the first catalog and merchant to associate with categories
        Catalog defaultCatalog = catalogs.isEmpty() ? null : catalogs.get(0);
        Merchant defaultMerchant = merchants.isEmpty() ? null : merchants.get(0);

        for (Map<String, String> data : categoryData) {
            try {
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
            } catch (Exception e) {
                log.error("Error creating category from data: {}", data, e);
            }
        }

        return categories;
    }

}
