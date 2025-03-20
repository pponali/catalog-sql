package com.nosql.poc.rules.client;

import com.nosql.poc.rules.client.fallback.ValidationServiceFallback;
import com.nosql.poc.rules.dto.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Feign client for calling the validation service
 */
@FeignClient(name = "validation-service", url = "${validation.service.url:http://localhost:8085}", fallback = ValidationServiceFallback.class)
public interface ValidationServiceClient {

    @CircuitBreaker(name = "validationService")
    @GetMapping("/api/validation-rules/{entityType}")
    ApiResponse<List<Map<String, Object>>> getValidationRulesForEntity(@PathVariable("entityType") String entityType);

    @CircuitBreaker(name = "validationService")
    @PostMapping("/api/validate/{entityType}")
    ApiResponse<Map<String, Object>> validateEntity(@PathVariable("entityType") String entityType, @RequestBody Map<String, Object> entity);
}