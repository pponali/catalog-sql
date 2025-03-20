package com.nosql.poc.vendor.controller;

import com.nosql.poc.vendor.service.DataImportService;
import com.nosql.poc.vendor.dto.ApiResponse;
import com.nosql.poc.vendor.dto.ImportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/mongodb/import")
@RequiredArgsConstructor
public class DataImportController {

    private final DataImportService dataImportService;

    @PostMapping("/vendors")
    public ResponseEntity<ApiResponse<ImportResult>> importVendors() {
        log.info("Importing vendors from CSV");
        ImportResult result = dataImportService.loadVendors();
        return createResponse(result);
    }

    @PostMapping("/product-sellers")
    public ResponseEntity<ApiResponse<ImportResult>> importProductSellers() {
        log.info("Importing product-sellers from CSV");
        ImportResult result = dataImportService.loadProductSellers();
        return createResponse(result);
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<Map<String, ImportResult>>> importAll() {
        log.info("Importing all vendor data from CSV files");
        Map<String, ImportResult> results = dataImportService.loadAllVendorData();
        
        // Count overall success
        int successCount = 0;
        int totalCount = results.size();
        for (ImportResult result : results.values()) {
            if (result.isSuccess()) {
                successCount++;
            }
        }
        
        boolean overallSuccess = successCount == totalCount;
        String message = String.format("Imported %d/%d vendor entity types successfully", successCount, totalCount);
        
        ApiResponse<Map<String, ImportResult>> response = new ApiResponse<>();
        response.setSuccess(overallSuccess);
        response.setMessage(message);
        response.setData(results);
        
        return ResponseEntity.ok(response);
    }
    
    private ResponseEntity<ApiResponse<ImportResult>> createResponse(ImportResult result) {
        String message = result.isSuccess() 
                ? String.format("Successfully imported %d %s entities", result.getImportedCount(), result.getEntityType())
                : String.format("Failed to import %s: %s", result.getEntityType(), result.getErrorMessage());
        
        ApiResponse<ImportResult> response = new ApiResponse<>();
        response.setSuccess(result.isSuccess());
        response.setMessage(message);
        response.setData(result);
        
        return ResponseEntity.ok(response);
    }
}