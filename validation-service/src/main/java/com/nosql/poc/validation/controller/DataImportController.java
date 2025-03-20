package com.nosql.poc.validation.controller;

import com.nosql.poc.validation.service.DataImportService;
import com.nosql.poc.validation.dto.ApiResponse;
import com.nosql.poc.validation.dto.ImportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/mongodb/import")
@RequiredArgsConstructor
public class DataImportController {

    private final DataImportService dataImportService;

    @PostMapping("/validation-rules")
    public ResponseEntity<ApiResponse<ImportResult>> importValidationRules() {
        log.info("Importing validation rules from CSV");
        ImportResult result = dataImportService.loadValidationRules();
        return createResponse(result);
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