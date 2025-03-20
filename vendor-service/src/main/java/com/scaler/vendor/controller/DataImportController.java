package com.scaler.vendor.controller;

import com.scaler.vendor.dto.ApiResponse;
import com.scaler.vendor.dto.ImportResult;
import com.scaler.vendor.util.LoadDataFromCsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/import")
@RequiredArgsConstructor
public class DataImportController {

    private final LoadDataFromCsvService loadDataFromCsvService;

    @PostMapping("/merchants")
    public ResponseEntity<ApiResponse<ImportResult>> importMerchants() {
        log.info("Importing merchants from CSV");
        ImportResult result = loadDataFromCsvService.loadMerchants();
        return createResponse(result);
    }

    @PostMapping("/sellers")
    public ResponseEntity<ApiResponse<ImportResult>> importSellers() {
        log.info("Importing sellers from CSV");
        ImportResult result = loadDataFromCsvService.loadSellers();
        return createResponse(result);
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<Map<String, ImportResult>>> importAll() {
        log.info("Importing all vendor data from CSV files");
        Map<String, ImportResult> results = loadDataFromCsvService.loadAllVendorData();
        
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
        
        ApiResponse<Map<String, ImportResult>> response = ApiResponse.<Map<String, ImportResult>>builder()
                .success(overallSuccess)
                .message(message)
                .data(results)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    private ResponseEntity<ApiResponse<ImportResult>> createResponse(ImportResult result) {
        String message = result.isSuccess() 
                ? String.format("Successfully imported %d %s entities", result.getImportedCount(), result.getEntityType())
                : String.format("Failed to import %s: %s", result.getEntityType(), result.getErrorMessage());
        
        ApiResponse<ImportResult> response = ApiResponse.<ImportResult>builder()
                .success(result.isSuccess())
                .message(message)
                .data(result)
                .build();
        
        return ResponseEntity.ok(response);
    }
}