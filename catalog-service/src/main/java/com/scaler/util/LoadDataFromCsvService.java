package com.scaler.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.scaler.dto.ImportResult;
import com.scaler.entity.*;
import com.scaler.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for loading catalog-related data from CSV files in the resources/csv directory
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoadDataFromCsvService {

    private final ResourceLoader resourceLoader;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;
    private final FeatureTemplateRepository featureTemplateRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final ProductFeatureRepository productFeatureRepository;
    private final ProductFeatureValueRepository productFeatureValueRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryFeatureTemplateRepository categoryFeatureTemplateRepository;

    /**
     * Load categories from categories.csv
     * 
     * @return ImportResult with details of the import operation
     */
    @Transactional
    public ImportResult loadCategories() {
        log.info("Loading categories from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("Category");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/categories.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<Category> categories = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        Category category = parseCategory(line, header);
                        categories.add(category);
                    } catch (Exception e) {
                        log.error("Error parsing category at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid categories
                if (!categories.isEmpty()) {
                    categoryRepository.saveAll(categories);
                    result.setSuccess(true);
                    result.setImportedCount(categories.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid categories found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading categories CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading categories resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load feature templates from feature_templates.csv
     * 
     * @return ImportResult with details of the import operation
     */
    @Transactional
    public ImportResult loadFeatureTemplates() {
        log.info("Loading feature templates from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("FeatureTemplate");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/feature_templates.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<FeatureTemplate> templates = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        FeatureTemplate template = parseFeatureTemplate(line, header);
                        templates.add(template);
                    } catch (Exception e) {
                        log.error("Error parsing feature template at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid templates
                if (!templates.isEmpty()) {
                    featureTemplateRepository.saveAll(templates);
                    result.setSuccess(true);
                    result.setImportedCount(templates.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid feature templates found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading feature templates CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading feature templates resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load units of measure from unit_of_measure.csv
     * 
     * @return ImportResult with details of the import operation
     */
    @Transactional
    public ImportResult loadUnitsOfMeasure() {
        log.info("Loading units of measure from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("UnitOfMeasure");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/unit_of_measure.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<UnitOfMeasure> units = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        UnitOfMeasure unit = parseUnitOfMeasure(line, header);
                        units.add(unit);
                    } catch (Exception e) {
                        log.error("Error parsing unit of measure at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid units
                if (!units.isEmpty()) {
                    unitOfMeasureRepository.saveAll(units);
                    result.setSuccess(true);
                    result.setImportedCount(units.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid units of measure found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading units of measure CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading units of measure resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * Load sample products from sample_products.csv
     * 
     * @return ImportResult with details of the import operation
     */
    @Transactional
    public ImportResult loadSampleProducts() {
        log.info("Loading sample products from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("Product");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/sample_products.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<Product> products = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        Product product = parseProduct(line, header);
                        products.add(product);
                    } catch (Exception e) {
                        log.error("Error parsing product at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid products
                if (!products.isEmpty()) {
                    productRepository.saveAll(products);
                    result.setSuccess(true);
                    result.setImportedCount(products.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid products found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading sample products CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading sample products resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load product features from product_features.csv
     * 
     * @return ImportResult with details of the import operation
     */
    @Transactional
    public ImportResult loadProductFeatures() {
        log.info("Loading product features from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("ProductFeature");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/product_features.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<ProductFeature> features = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        ProductFeature feature = parseProductFeature(line, header);
                        features.add(feature);
                    } catch (Exception e) {
                        log.error("Error parsing product feature at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid features
                if (!features.isEmpty()) {
                    productFeatureRepository.saveAll(features);
                    result.setSuccess(true);
                    result.setImportedCount(features.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid product features found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading product features CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading product features resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load category feature templates from category_feature_templates.csv
     */
    @Transactional
    public ImportResult loadCategoryFeatureTemplates() {
        log.info("Loading category feature templates from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("CategoryFeatureTemplate");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/category_feature_templates.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<CategoryFeatureTemplate> templates = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        Map<String, String> rowMap = mapRowToHeader(line, header);
                        
                        CategoryFeatureTemplate template = new CategoryFeatureTemplate();
                        
                        String id = rowMap.get("id");
                        if (id != null && !id.isEmpty()) {
                            template.setId(UUID.fromString(id));
                        } else {
                            template.setId(UUID.randomUUID());
                        }
                        
                        // Get category
                        String categoryId = requireField(rowMap, "category_id");
                        Optional<Category> category = categoryRepository.findById(UUID.fromString(categoryId));
                        if (category.isEmpty()) {
                            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
                        }
                        template.setCategory(category.get());
                        
                        // Get feature template
                        String templateId = requireField(rowMap, "feature_template_id");
                        Optional<FeatureTemplate> featureTemplate = featureTemplateRepository.findById(UUID.fromString(templateId));
                        if (featureTemplate.isEmpty()) {
                            throw new IllegalArgumentException("Feature template not found with ID: " + templateId);
                        }
                        template.setFeatureTemplate(featureTemplate.get());
                        
                        String required = rowMap.get("required");
                        if (required != null && !required.isEmpty()) {
                            template.setRequired(Boolean.parseBoolean(required));
                        }
                        
                        String displayOrder = rowMap.get("display_order");
                        if (displayOrder != null && !displayOrder.isEmpty()) {
                            template.setDisplayOrder(Integer.parseInt(displayOrder));
                        }
                        
                        template.setCreatedDate(LocalDateTime.now());
                        template.setLastModifiedDate(LocalDateTime.now());
                        
                        templates.add(template);
                    } catch (Exception e) {
                        log.error("Error parsing category feature template at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid templates
                if (!templates.isEmpty()) {
                    categoryFeatureTemplateRepository.saveAll(templates);
                    result.setSuccess(true);
                    result.setImportedCount(templates.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid category feature templates found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading category feature templates CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading category feature templates resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load product category mappings from product_category_mappings.csv
     */
    @Transactional
    public ImportResult loadProductCategoryMappings() {
        log.info("Loading product category mappings from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("ProductCategory");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/product_category_mappings.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<ProductCategory> mappings = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        Map<String, String> rowMap = mapRowToHeader(line, header);
                        
                        ProductCategory mapping = new ProductCategory();
                        
                        String id = rowMap.get("id");
                        if (id != null && !id.isEmpty()) {
                            mapping.setId(UUID.fromString(id));
                        } else {
                            mapping.setId(UUID.randomUUID());
                        }
                        
                        // Get product
                        String productId = requireField(rowMap, "product_id");
                        Optional<Product> product = productRepository.findById(UUID.fromString(productId));
                        if (product.isEmpty()) {
                            throw new IllegalArgumentException("Product not found with ID: " + productId);
                        }
                        mapping.setProduct(product.get());
                        
                        // Get category
                        String categoryId = requireField(rowMap, "category_id");
                        Optional<Category> category = categoryRepository.findById(UUID.fromString(categoryId));
                        if (category.isEmpty()) {
                            throw new IllegalArgumentException("Category not found with ID: " + categoryId);
                        }
                        mapping.setCategory(category.get());
                        
                        String isPrimary = rowMap.get("is_primary");
                        if (isPrimary != null && !isPrimary.isEmpty()) {
                            mapping.setIsPrimary(Boolean.parseBoolean(isPrimary));
                        }
                        
                        mapping.setCreatedDate(LocalDateTime.now());
                        mapping.setLastModifiedDate(LocalDateTime.now());
                        
                        mappings.add(mapping);
                    } catch (Exception e) {
                        log.error("Error parsing product category mapping at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid mappings
                if (!mappings.isEmpty()) {
                    productCategoryRepository.saveAll(mappings);
                    result.setSuccess(true);
                    result.setImportedCount(mappings.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid product category mappings found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading product category mappings CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading product category mappings resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * Parse a category from a CSV row
     */
    private Category parseCategory(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        Category category = new Category();
        
        String id = rowMap.get("id");
        if (id != null && !id.isEmpty()) {
            category.setId(UUID.fromString(id));
        } else {
            category.setId(UUID.randomUUID());
        }
        
        category.setName(requireField(rowMap, "name"));
        category.setCode(requireField(rowMap, "code"));
        
        String description = rowMap.get("description");
        if (description != null) {
            category.setDescription(description);
        }
        
        String status = rowMap.get("status");
        if (status != null) {
            category.setStatus(status);
        } else {
            category.setStatus("ACTIVE");
        }
        
        String parentId = rowMap.get("parent_id");
        if (parentId != null && !parentId.isEmpty()) {
            Optional<Category> parent = categoryRepository.findById(UUID.fromString(parentId));
            parent.ifPresent(category::setParent);
        }
        
        category.setCreatedDate(LocalDateTime.now());
        category.setLastModifiedDate(LocalDateTime.now());
        
        return category;
    }
    
    /**
     * Parse a feature template from a CSV row
     */
    private FeatureTemplate parseFeatureTemplate(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        FeatureTemplate template = new FeatureTemplate();
        
        String id = rowMap.get("id");
        if (id != null && !id.isEmpty()) {
            template.setId(UUID.fromString(id));
        } else {
            template.setId(UUID.randomUUID());
        }
        
        template.setName(requireField(rowMap, "name"));
        template.setCode(requireField(rowMap, "code"));
        
        String description = rowMap.get("description");
        if (description != null) {
            template.setDescription(description);
        }
        
        String attributeType = rowMap.get("attribute_type");
        if (attributeType != null) {
            template.setAttributeType(attributeType);
        }
        
        String validationPattern = rowMap.get("validation_pattern");
        if (validationPattern != null) {
            template.setValidationPattern(validationPattern);
        }
        
        String minValue = rowMap.get("min_value");
        if (minValue != null) {
            template.setMinValue(minValue);
        }
        
        String maxValue = rowMap.get("max_value");
        if (maxValue != null) {
            template.setMaxValue(maxValue);
        }
        
        String allowedValues = rowMap.get("allowed_values");
        if (allowedValues != null) {
            template.setAllowedValues(allowedValues);
        }
        
        String defaultValue = rowMap.get("default_value");
        if (defaultValue != null) {
            template.setDefaultValue(defaultValue);
        }
        
        template.setCreatedDate(LocalDateTime.now());
        template.setLastModifiedDate(LocalDateTime.now());
        
        return template;
    }
    
    /**
     * Parse a unit of measure from a CSV row
     */
    private UnitOfMeasure parseUnitOfMeasure(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        UnitOfMeasure unit = new UnitOfMeasure();
        
        String id = rowMap.get("id");
        if (id != null && !id.isEmpty()) {
            unit.setId(UUID.fromString(id));
        } else {
            unit.setId(UUID.randomUUID());
        }
        
        unit.setName(requireField(rowMap, "name"));
        unit.setCode(requireField(rowMap, "code"));
        
        String description = rowMap.get("description");
        if (description != null) {
            unit.setDescription(description);
        }
        
        String conversionFactorStr = rowMap.get("conversion_factor");
        if (conversionFactorStr != null && !conversionFactorStr.isEmpty()) {
            try {
                unit.setConversionFactor(Double.parseDouble(conversionFactorStr));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid conversion factor: " + conversionFactorStr);
            }
        }
        
        String baseUnitCode = rowMap.get("base_unit_code");
        if (baseUnitCode != null && !baseUnitCode.isEmpty()) {
            Unit baseUnit = new Unit();
            baseUnit.setCode(baseUnitCode);
            unit.setBaseUnit(baseUnit);
        }
        
        unit.setCreatedDate(LocalDateTime.now());
        unit.setLastModifiedDate(LocalDateTime.now());
        
        return unit;
    }
    
    /**
     * Parse a product from a CSV row
     */
    private Product parseProduct(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        Product product = new Product();
        
        String id = rowMap.get("product_id");
        if (id != null && !id.isEmpty()) {
            product.setId(UUID.fromString(id));
        } else {
            product.setId(UUID.randomUUID());
        }
        
        product.setName(requireField(rowMap, "product_name"));
        product.setCode(requireField(rowMap, "product_code"));
        product.setSku(requireField(rowMap, "sku"));
        
        String description = rowMap.get("description");
        if (description != null) {
            product.setDescription(description);
        }
        
        String status = rowMap.get("status");
        if (status != null) {
            product.setStatus(status);
        } else {
            product.setStatus("ACTIVE");
        }
        
        String productType = rowMap.get("product_type");
        if (productType != null && !productType.isEmpty()) {
            try {
                product.setProductType(ProductType.valueOf(productType.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid product type: " + productType);
            }
        } else {
            // Default to SIMPLE if not specified
            product.setProductType(ProductType.SIMPLE);
        }
        
        // Set merchant if provided
        String merchantId = rowMap.get("merchant_id");
        if (merchantId != null && !merchantId.isEmpty()) {
            Optional<Merchant> merchant = merchantRepository.findById(UUID.fromString(merchantId));
            merchant.ifPresent(product::setMerchant);
        }
        
        product.setCreatedDate(LocalDateTime.now());
        product.setLastModifiedDate(LocalDateTime.now());
        
        return product;
    }
    
    /**
     * Parse a product feature from a CSV row
     */
    private ProductFeature parseProductFeature(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        ProductFeature feature = new ProductFeature();
        
        String id = rowMap.get("id");
        if (id != null && !id.isEmpty()) {
            feature.setId(UUID.fromString(id));
        } else {
            feature.setId(UUID.randomUUID());
        }
        
        feature.setName(requireField(rowMap, "name"));
        feature.setCode(requireField(rowMap, "code"));
        
        String description = rowMap.get("description");
        if (description != null) {
            feature.setDescription(description);
        }
        
        String attributeType = rowMap.get("attribute_type");
        if (attributeType != null) {
            feature.setAttributeType(attributeType);
        }
        
        String validationPattern = rowMap.get("validation_pattern");
        if (validationPattern != null) {
            feature.setValidationPattern(validationPattern);
        }
        
        String minValue = rowMap.get("min_value");
        if (minValue != null) {
            feature.setMinValue(minValue);
        }
        
        String maxValue = rowMap.get("max_value");
        if (maxValue != null) {
            feature.setMaxValue(maxValue);
        }
        
        String allowedValues = rowMap.get("allowed_values");
        if (allowedValues != null) {
            feature.setAllowedValues(allowedValues);
        }
        
        String defaultValue = rowMap.get("default_value");
        if (defaultValue != null) {
            feature.setDefaultValue(defaultValue);
        }
        
        String templateIdStr = rowMap.get("template_id");
        if (templateIdStr != null && !templateIdStr.isEmpty()) {
            UUID templateId = UUID.fromString(templateIdStr);
            feature.setTemplateId(templateId);
        }
        
        feature.setCreatedDate(LocalDateTime.now());
        feature.setLastModifiedDate(LocalDateTime.now());
        
        return feature;
    }
    
    /**
     * Map a CSV row to column headers
     */
    private Map<String, String> mapRowToHeader(String[] row, String[] header) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < Math.min(row.length, header.length); i++) {
            map.put(header[i], row[i]);
        }
        return map;
    }
    
    /**
     * Get a required field from the row map
     */
    private String requireField(Map<String, String> map, String fieldName) {
        String value = map.get(fieldName);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Required field missing: " + fieldName);
        }
        return value;
    }
    
    /**
     * Load all catalog data from CSV files
     * 
     * @return Map of entity types to import results
     */
    @Transactional
    public Map<String, ImportResult> loadAllCatalogData() {
        log.info("Loading all catalog-related data from CSV files");
        
        Map<String, ImportResult> results = new HashMap<>();
        
        // Load in a specific order to handle dependencies
        results.put("Categories", loadCategories());
        results.put("FeatureTemplates", loadFeatureTemplates());
        results.put("UnitsOfMeasure", loadUnitsOfMeasure());
        results.put("Products", loadSampleProducts());
        results.put("ProductFeatures", loadProductFeatures());
        results.put("CategoryFeatureTemplates", loadCategoryFeatureTemplates());
        results.put("ProductCategoryMappings", loadProductCategoryMappings());
        
        return results;
    }
}