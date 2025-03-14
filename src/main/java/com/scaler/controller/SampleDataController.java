package com.scaler.controller;

import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import com.scaler.util.ProductCategoryMappingImporter;
import com.scaler.util.SampleProductImporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Controller for importing sample data
 */
@Slf4j
@RestController
@RequestMapping("/api/sample-data")
@RequiredArgsConstructor
public class SampleDataController {

    private final SampleProductImporter sampleProductImporter;
    private final ProductCategoryMappingImporter productCategoryMappingImporter;

    /**
     * Import sample products
     * 
     * @return List of imported products
     */
    @PostMapping("/import-products")
    public ResponseEntity<List<Product>> importSampleProducts() {
        log.info("Importing sample products");
        List<Product> importedProducts = sampleProductImporter.importSampleProducts();
        log.info("Imported {} sample products", importedProducts.size());
        return ResponseEntity.ok(importedProducts);
    }
    
    /**
     * Import product-category mappings
     * 
     * @return List of imported product-category mappings
     */
    @PostMapping("/import-product-category-mappings")
    public ResponseEntity<List<ProductCategory>> importProductCategoryMappings() {
        log.info("Importing product-category mappings");
        List<ProductCategory> importedMappings = productCategoryMappingImporter.importProductCategoryMappings();
        log.info("Imported {} product-category mappings", importedMappings.size());
        return ResponseEntity.ok(importedMappings);
    }
    
    /**
     * Import all sample data (products and mappings)
     * 
     * @return Map containing imported data
     */
    @PostMapping("/import-all")
    public ResponseEntity<Map<String, Object>> importAllSampleData() {
        log.info("Importing all sample data");
        
        // Import products
        List<Product> importedProducts = sampleProductImporter.importSampleProducts();
        log.info("Imported {} sample products", importedProducts.size());
        
        // Import product-category mappings
        List<ProductCategory> importedMappings = productCategoryMappingImporter.importProductCategoryMappings();
        log.info("Imported {} product-category mappings", importedMappings.size());
        
        // Return results
        Map<String, Object> results = Map.of(
            "products", importedProducts,
            "productCategoryMappings", importedMappings
        );
        
        return ResponseEntity.ok(results);
    }
}
