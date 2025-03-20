package com.nosql.poc.rules.client.fallback;

import com.nosql.poc.rules.client.ValidationServiceClient;
import com.nosql.poc.rules.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Fallback implementation for the ValidationServiceClient when the service is unavailable
 */
@Slf4j
@Component
public class ValidationServiceFallback implements ValidationServiceClient {

    @Override
    public ApiResponse<List<Map<String, Object>>> getValidationRulesForEntity(String entityType) {
        log.warn("Fallback: Unable to get validation rules for entity type: {}", entityType);
        
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Validation service is currently unavailable. Using default rules.");
        response.setErrorCode("SERVICE_UNAVAILABLE");
        response.setData(Collections.emptyList());
        
        return response;
    }

    @Override
    public ApiResponse<Map<String, Object>> validateEntity(String entityType, Map<String, Object> entity) {
        log.warn("Fallback: Unable to validate entity of type: {}", entityType);
        
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setSuccess(true); // Assume valid in fallback
        response.setMessage("Validation service is currently unavailable. Entity validation skipped.");
        response.setErrorCode("SERVICE_UNAVAILABLE");
        response.setData(Map.of(
            "valid", true,
            "skipped", true,
            "reason", "Validation service unavailable"
        ));
        
        return response;
    }
}