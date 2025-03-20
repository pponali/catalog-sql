package com.scaler.controller;

import com.scaler.dto.ApiResponse;
import com.scaler.dto.ImportResult;
import com.scaler.util.LoadDataFromCsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
@Tag(name = "Catalog Data Import", description = "APIs for importing catalog-related data from CSV files")
public class DataImportController {

    private final LoadDataFromCsvService loadDataFromCsvService;

    @PostMapping("/categories")
    @Operation(summary = "Import categories from CSV", description = "Loads category data from the categories.csv file")
    public ResponseEntity<ApiResponse<ImportResult>> importCategories() {
        log.info("Importing categories from CSV");
        ImportResult result = loadDataFromCsvService.loadCategories();
        return createResponse(result);
    }

    @PostMapping("/feature-templates")
    @Operation(summary = "Import feature templates from CSV", description = "Loads feature template data from the feature_templates.csv file")
    public ResponseEntity<ApiResponse<ImportResult>> importFeatureTemplates() {
        log.info("Importing feature templates from CSV");
        ImportResult result = loadDataFromCsvService.loadFeatureTemplates();
        return createResponse(result);
    }

    @PostMapping("/units-of-measure")
    @Operation(summary = "Import units of measure from CSV", description = "Loads unit of measure data from the unit_of_measure.csv file")
    public ResponseEntity<ApiResponse<ImportResult>> importUnitsOfMeasure() {
        log.info("Importing units of measure from CSV");
        ImportResult result = loadDataFromCsvService.loadUnitsOfMeasure();
        return createResponse(result);
    }

    @PostMapping("/products")
    @Operation(summary = "Import sample products from CSV", description = "Loads sample product data from the sample_products.csv file")
    public ResponseEntity<ApiResponse<ImportResult>> importProducts() {
        log.info("Importing sample products from CSV");
        ImportResult result = loadDataFromCsvService.loadSampleProducts();
        return createResponse(result);
    }

    @PostMapping("/product-features")
    @Operation(summary = "Import product features from CSV", description = "Loads product feature data from the product_features.csv file")
    public ResponseEntity<ApiResponse<ImportResult>> importProductFeatures() {
        log.info("Importing product features from CSV");
        ImportResult result = loadDataFromCsvService.loadProductFeatures();
        return createResponse(result);
    }
    
    @PostMapping("/category-feature-templates")
    @Operation(summary = "Import category feature templates from CSV", description = "Loads category feature template mappings from the category_feature_templates.csv file")
    public ResponseEntity<ApiResponse<ImportResult>> importCategoryFeatureTemplates() {
        log.info("Importing category feature templates from CSV");
        ImportResult result = loadDataFromCsvService.loadCategoryFeatureTemplates();
        return createResponse(result);
    }
    
    @PostMapping("/product-category-mappings")
    @Operation(summary = "Import product category mappings from CSV", description = "Loads product category mappings from the product_category_mappings.csv file")
    public ResponseEntity<ApiResponse<ImportResult>> importProductCategoryMappings() {
        log.info("Importing product category mappings from CSV");
        ImportResult result = loadDataFromCsvService.loadProductCategoryMappings();
        return createResponse(result);
    }

    @PostMapping("/all")
    @Operation(summary = "Import all catalog data from CSV", description = "Loads all catalog-related data from CSV files in the correct order")
    public ResponseEntity<ApiResponse<Map<String, ImportResult>>> importAll() {
        log.info("Importing all catalog data from CSV files");
        Map<String, ImportResult> results = loadDataFromCsvService.loadAllCatalogData();
        
        // Count overall success
        int successCount = 0;
        int totalCount = results.size();
        for (ImportResult result : results.values()) {
            if (result.isSuccess()) {
                successCount++;
            }
        }
        
        boolean overallSuccess = successCount == totalCount;
        String message = String.format("Imported %d/%d catalog entity types successfully", successCount, totalCount);
        
        ApiResponse<Map<String, ImportResult>> response = ApiResponse.<Map<String, ImportResult>>builder()
                .status(overallSuccess ? "SUCCESS" : "ERROR")
                .message(message)
                .data(results)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    private ResponseEntity<ApiResponse<ImportResult>> createResponse(ImportResult result) {
        String message = result.isSuccess() 
                ? String.format("Successfully imported %d %s entities", result.getImportedCount(), result.getEntityType())
                : String.format("Failed to import %s: %s", result.getEntityType(), result.getErrorMessage());
        
        ApiResponse<ImportResult> response = ApiResponse.<ImportResult>builder()
                .status(result.isSuccess() ? "SUCCESS" : "ERROR")
                .message(message)
                .data(result)
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(response);
    }
}