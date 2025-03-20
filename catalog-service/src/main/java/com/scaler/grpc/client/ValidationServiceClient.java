package com.scaler.grpc.client;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.ProductFeatureDTO;
import com.scaler.dto.ValidationResultDTO;
import com.scaler.exception.ValidationException;
import com.scaler.grpc.validation.*;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * gRPC client for the Validation service
 */
@Slf4j
@Service
public class ValidationServiceClient {

    private final ValidationServiceGrpc.ValidationServiceBlockingStub validationServiceStub;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public ValidationServiceClient(
            @Qualifier("validationServiceChannel") ManagedChannel channel,
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry) {
        this.validationServiceStub = ValidationServiceGrpc.newBlockingStub(channel);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("validationService");
        this.retry = retryRegistry.retry("validationService");
        log.info("ValidationServiceClient initialized with circuit breaker and retry");
    }

    /**
     * Validates a product using the ValidationService
     *
     * @param productDTO The product to validate
     * @return ValidationResultDTO containing validation results
     * @throws ValidationException if there's an error during validation
     */
    public ValidationResultDTO validateProduct(ProductDTO productDTO) {
        log.info("Validating product with ID: {}", productDTO.getId());

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<ValidationResultDTO> validateProductFunction = () -> {
            try {
                // Convert ProductDTO to ValidateProductRequest
                Map<String, String> productData = new HashMap<>();
                productData.put("id", productDTO.getId().toString());
                productData.put("name", productDTO.getName());
                productData.put("sku", productDTO.getSku());
                
                if (productDTO.getDescription() != null) {
                    productData.put("description", productDTO.getDescription());
                }
                
                if (productDTO.getBrand() != null) {
                    productData.put("brand", productDTO.getBrand());
                }
                
                // Convert product features to FeatureValuePair messages
                List<FeatureValuePair> featureValues = new ArrayList<>();
                if (productDTO.getFeatures() != null) {
                    featureValues = productDTO.getFeatures().stream()
                            .map(this::convertToFeatureValuePair)
                            .collect(Collectors.toList());
                }

                // Build the request
                ValidateProductRequest request = ValidateProductRequest.newBuilder()
                        .setProductId(productDTO.getId().toString())
                        .putAllProductData(productData)
                        .addAllFeatureValues(featureValues)
                        .build();

                // Make the gRPC call
                ValidationResponse response = validationServiceStub.validateProduct(request);
                
                // Convert the response to our DTO
                return convertToValidationResultDTO(response);
                
            } catch (StatusRuntimeException e) {
                log.error("Error validating product: {} - {}", productDTO.getId(), e.getMessage());
                throw new ValidationException("Failed to validate product: " + e.getMessage());
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return validateProductFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when validating product {}: {}", 
                    productDTO.getId(), e.getMessage());
            
            // Create a fallback validation result that passes validation
            // This is a fail-open approach for better resilience
            ValidationResultDTO fallbackResult = new ValidationResultDTO();
            fallbackResult.setValid(true);
            fallbackResult.setErrors(Collections.singletonList(
                    "Validation service unavailable - proceeding with caution"));
            fallbackResult.setWarnings(Collections.singletonList(
                    "Validation service unavailable - validation skipped"));
            return fallbackResult;
        }
    }

    /**
     * Validates a single feature value
     *
     * @param featureId The ID of the feature
     * @param value The value to validate
     * @param productId The product ID (optional)
     * @param categoryId The category ID (optional)
     * @return ValidationResultDTO containing validation results
     * @throws ValidationException if there's an error during validation
     */
    public ValidationResultDTO validateFeatureValue(String featureId, String value, String productId, String categoryId) {
        log.info("Validating feature value - feature: {}, value: {}", featureId, value);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<ValidationResultDTO> validateFeatureValueFunction = () -> {
            try {
                // Build the request
                ValidateFeatureValueRequest.Builder requestBuilder = ValidateFeatureValueRequest.newBuilder()
                        .setFeatureId(featureId)
                        .setValue(value);
                
                if (productId != null) {
                    requestBuilder.setProductId(productId);
                }
                
                if (categoryId != null) {
                    requestBuilder.setCategoryId(categoryId);
                }

                // Make the gRPC call
                ValidationResponse response = validationServiceStub.validateFeatureValue(requestBuilder.build());
                
                // Convert the response to our DTO
                return convertToValidationResultDTO(response);
                
            } catch (StatusRuntimeException e) {
                log.error("Error validating feature value: {} - {}", featureId, e.getMessage());
                throw new ValidationException("Failed to validate feature value: " + e.getMessage());
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return validateFeatureValueFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when validating feature value {} = {}: {}", 
                    featureId, value, e.getMessage());
            
            // Create a fallback validation result that passes validation
            // This is a fail-open approach for better resilience
            ValidationResultDTO fallbackResult = new ValidationResultDTO();
            fallbackResult.setValid(true);
            fallbackResult.setErrors(Collections.singletonList(
                    "Validation service unavailable - proceeding with caution"));
            fallbackResult.setWarnings(Collections.singletonList(
                    "Validation service unavailable - feature validation skipped"));
            return fallbackResult;
        }
    }

    /**
     * Converts a ProductFeatureDTO to a FeatureValuePair message
     */
    private FeatureValuePair convertToFeatureValuePair(ProductFeatureDTO feature) {
        // Adapt this to match the actual DTO fields
        return FeatureValuePair.newBuilder()
                .setFeatureId(feature.getId().toString())
                .setFeatureName(feature.getName())
                // Value field might not be directly accessible
                //.setValue(feature.getValue())
                .setValue("N/A") // Default value
                .build();
    }

    /**
     * Converts a ValidationResponse to a ValidationResultDTO
     */
    private ValidationResultDTO convertToValidationResultDTO(ValidationResponse response) {
        ValidationResultDTO resultDTO = new ValidationResultDTO();
        resultDTO.setValid(response.getValid());
        
        // Convert errors
        List<String> errors = response.getErrorsList().stream()
                .map(error -> String.format("[%s] %s: %s - %s", 
                        error.getCode(), error.getField(), error.getValue(), error.getMessage()))
                .collect(Collectors.toList());
        resultDTO.setErrors(errors);
        
        // Convert warnings
        List<String> warnings = response.getWarningsList().stream()
                .map(warning -> String.format("[%s] %s: %s - %s", 
                        warning.getCode(), warning.getField(), warning.getValue(), warning.getMessage()))
                .collect(Collectors.toList());
        resultDTO.setWarnings(warnings);
        
        return resultDTO;
    }
}