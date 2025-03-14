package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.builder.ProductCategoryBuilder;
import com.scaler.entity.*;
import com.scaler.repository.CatalogRepository;
import com.scaler.repository.CategoryRepository;
import com.scaler.repository.MerchantRepository;
import com.scaler.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to import sample products from CSV file
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SampleProductImporter {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final CatalogRepository catalogRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Import sample products from CSV file
     * 
     * @return List of imported products
     */
    @Transactional
    public List<Product> importSampleProducts() {
        log.info("Importing sample products from CSV file");
        List<Product> importedProducts = new ArrayList<>();
        
        try (InputStream is = getClass().getResourceAsStream("/csv/sample_products.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            // Skip header row
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                try {
                    Product product = convertCsvLineToProduct(line);
                    if (product != null) {
                        importedProducts.add(productRepository.save(product));
                    }
                } catch (Exception e) {
                    log.error("Error importing product from CSV line: {}", line, e);
                }
            }
            
        } catch (IOException e) {
            log.error("Error reading sample_products.csv file", e);
        }
        
        log.info("Imported {} sample products", importedProducts.size());
        return importedProducts;
    }
    
    /**
     * Convert CSV line to Product entity
     * 
     * @param line CSV line
     * @return Product entity
     */
    private Product convertCsvLineToProduct(String line) throws JsonProcessingException {
        String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        // Extract basic product information
        String productCode = values[1];
        String productName = values[2];
        String description = values[3];
        ProductType productType = ProductType.valueOf(values[4]);
        String status = values[5];
        Double price = Double.parseDouble(values[6]);
        String sku = values[7];
        String categoryCode = values[8];
        String merchantCode = values[9];
        String catalogCode = values[10];
        
        // Find merchant, category, and catalog
        Merchant merchant = merchantRepository.findByCode(merchantCode)
                .orElseThrow(() -> new IllegalStateException("Merchant not found: " + merchantCode));
        
        Category category = categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new IllegalStateException("Category not found: " + categoryCode));
        
        Catalog catalog = catalogRepository.findByCode(catalogCode)
                .orElseThrow(() -> new IllegalStateException("Catalog not found: " + catalogCode));
        
        // Create metadata from remaining attributes
        ObjectNode metadata = objectMapper.createObjectNode();
        
        // Add attributes to metadata
        if (values.length > 11) {
            metadata.put("colorfamilyapparel", getValueOrEmpty(values, 11));
            metadata.put("stylecode", getValueOrEmpty(values, 12));
            metadata.put("colorapparel", getValueOrEmpty(values, 13));
            metadata.put("sizechart", getValueOrEmpty(values, 14));
            metadata.put("occasion", getValueOrEmpty(values, 15));
            metadata.put("featureapparel", getValueOrEmpty(values, 16));
            metadata.put("stylenote", getValueOrEmpty(values, 17));
            metadata.put("modelfit", getValueOrEmpty(values, 18));
            metadata.put("fabricapparel", getValueOrEmpty(values, 19));
            metadata.put("washcare", getValueOrEmpty(values, 20));
            metadata.put("multipack", getValueOrEmpty(values, 21));
            metadata.put("packquantity", getValueOrEmpty(values, 22));
            metadata.put("ageband", getValueOrEmpty(values, 23));
            metadata.put("displayproduct", getValueOrEmpty(values, 24));
            metadata.put("unisexapparel", getValueOrEmpty(values, 25));
        }
        
        // Create product
        Product product = Product.builder()
                .code(productCode)
                .name(productName)
                .description(description)
                .productType(productType)
                .status(status)
                .merchant(merchant)
                .catalog(catalog)
                .price(price)
                .sku(sku)
                .metadata(metadata.toString())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        // Add category
        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, category, merchant);
        product.getProductCategories().add(productCategory);
        
        return product;
    }
    
    /**
     * Get value from array or empty string if index is out of bounds
     * 
     * @param values Array of values
     * @param index Index to get
     * @return Value or empty string
     */
    private String getValueOrEmpty(String[] values, int index) {
        if (index < values.length) {
            String value = values[index].trim();
            // Remove quotes if present
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            return value;
        }
        return "";
    }
}
