package com.scaler.util;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import com.scaler.repository.CategoryRepository;
import com.scaler.repository.ProductCategoryRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Utility class to import product-category mappings from CSV file
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCategoryMappingImporter {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    /**
     * Import product-category mappings from CSV file
     * 
     * @return List of imported product-category mappings
     */
    @Transactional
    public List<ProductCategory> importProductCategoryMappings() {
        log.info("Importing product-category mappings from CSV file");
        List<ProductCategory> importedMappings = new ArrayList<>();
        
        try (InputStream is = getClass().getResourceAsStream("/csv/product_category_mappings.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            // Skip header row
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                try {
                    ProductCategory mapping = convertCsvLineToProductCategory(line);
                    if (mapping != null) {
                        importedMappings.add(productCategoryRepository.save(mapping));
                    }
                } catch (Exception e) {
                    log.error("Error importing product-category mapping from CSV line: {}", line, e);
                }
            }
            
        } catch (IOException e) {
            log.error("Error reading product_category_mappings.csv file", e);
        }
        
        log.info("Imported {} product-category mappings", importedMappings.size());
        return importedMappings;
    }
    
    /**
     * Convert CSV line to ProductCategory entity
     * 
     * @param line CSV line
     * @return ProductCategory entity
     */
    private ProductCategory convertCsvLineToProductCategory(String line) {
        String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        // Extract mapping information
        String productCode = values[1];
        String categoryCode = values[2];
        int displayOrder = Integer.parseInt(values[4]);
        boolean primaryCategory = Boolean.parseBoolean(values[5]);
        
        // Find product and category
        Optional<Product> productOpt = productRepository.findByCode(productCode);
        Optional<Category> categoryOpt = categoryRepository.findByCode(categoryCode);
        
        if (productOpt.isEmpty()) {
            log.error("Product not found: {}", productCode);
            return null;
        }
        
        if (categoryOpt.isEmpty()) {
            log.error("Category not found: {}", categoryCode);
            return null;
        }
        
        Product product = productOpt.get();
        Category category = categoryOpt.get();
        
        // Check if mapping already exists
        Optional<ProductCategory> existingMapping = productCategoryRepository.findByProductAndCategory(product, category);
        if (existingMapping.isPresent()) {
            log.info("Mapping already exists for product {} and category {}", productCode, categoryCode);
            return existingMapping.get();
        }
        
        // Create new mapping
        ProductCategory productCategory = ProductCategory.builder()
                .product(product)
                .category(category)
                .displayOrder(displayOrder)
                .isPrimary(primaryCategory)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        return productCategory;
    }
}
