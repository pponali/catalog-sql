package com.nosql.poc.channel.controller;

import com.nosql.poc.channel.service.DataImportService;
import com.nosql.poc.channel.dto.ApiResponse;
import com.nosql.poc.channel.dto.ImportResult;
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

    @PostMapping("/channels")
    public ResponseEntity<ApiResponse<ImportResult>> importChannels() {
        log.info("Importing channels from CSV");
        ImportResult result = dataImportService.loadChannels();
        return createResponse(result);
    }

    @PostMapping("/channel-products")
    public ResponseEntity<ApiResponse<ImportResult>> importChannelProducts() {
        log.info("Importing channel products from CSV");
        ImportResult result = dataImportService.loadChannelProducts();
        return createResponse(result);
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<Map<String, ImportResult>>> importAll() {
        log.info("Importing all channel data from CSV files");
        Map<String, ImportResult> results = dataImportService.loadAllChannelData();
        
        // Count overall success
        int successCount = 0;
        int totalCount = results.size();
        for (ImportResult result : results.values()) {
            if (result.isSuccess()) {
                successCount++;
            }
        }
        
        boolean overallSuccess = successCount == totalCount;
        String message = String.format("Imported %d/%d channel entity types successfully", successCount, totalCount);
        
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