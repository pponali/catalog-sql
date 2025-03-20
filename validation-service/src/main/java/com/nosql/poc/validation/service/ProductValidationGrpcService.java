package com.nosql.poc.validation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosql.poc.validation.ValidationService;
import com.nosql.poc.validation.model.ValidationError;
import com.nosql.poc.validation.model.ValidationResult;
import com.nosql.poc.validation.model.ValidationWarning;
import com.scaler.grpc.validation.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * gRPC service implementation for product validation
 */
@Slf4j
@GrpcService
public class ProductValidationGrpcService extends ValidationServiceGrpc.ValidationServiceImplBase {

    private final ValidationService validationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ProductValidationGrpcService(ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public void validateProduct(ValidateProductRequest request, StreamObserver<ValidationResponse> responseObserver) {
        try {
            log.info("Received gRPC request to validate product: {}", request.getProductId());
            
            // Extract product data from request
            Map<String, Object> productData = new HashMap<>();
            request.getProductDataMap().forEach(productData::put);
            
            // Add feature values
            for (FeatureValuePair pair : request.getFeatureValuesList()) {
                productData.put(pair.getFeatureName(), pair.getValue());
            }
            
            // Use the validation service to validate the product
            ValidationResult result = validationService.validateProduct(productData);
            
            // Convert to gRPC response
            ValidationResponse.Builder responseBuilder = ValidationResponse.newBuilder()
                    .setValid(result.isValid());
            
            // Add errors
            if (result.getErrors() != null) {
                for (ValidationError error : result.getErrors()) {
                    com.scaler.grpc.validation.ValidationError grpcError = com.scaler.grpc.validation.ValidationError.newBuilder()
                            .setCode(error.getCode() != null ? error.getCode() : "UNKNOWN")
                            .setMessage(error.getMessage() != null ? error.getMessage() : "")
                            .setField(error.getField() != null ? error.getField() : "")
                            .setValue(error.getInvalidValue() != null ? error.getInvalidValue().toString() : "")
                            .build();
                    responseBuilder.addErrors(grpcError);
                }
            }
            
            // Add warnings
            if (result.getWarnings() != null) {
                for (ValidationWarning warning : result.getWarnings()) {
                    com.scaler.grpc.validation.ValidationWarning grpcWarning = com.scaler.grpc.validation.ValidationWarning.newBuilder()
                            .setCode(warning.getCode() != null ? warning.getCode() : "UNKNOWN")
                            .setMessage(warning.getMessage() != null ? warning.getMessage() : "")
                            .setField(warning.getField() != null ? warning.getField() : "")
                            .setValue(warning.getValue() != null ? warning.getValue().toString() : "")
                            .build();
                    responseBuilder.addWarnings(grpcWarning);
                }
            }
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error validating product via gRPC", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error validating product: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void validateFeatureValue(ValidateFeatureValueRequest request, StreamObserver<ValidationResponse> responseObserver) {
        try {
            log.info("Received gRPC request to validate feature value - feature: {}, value: {}", 
                    request.getFeatureId(), request.getValue());
            
            // Create a feature data map
            Map<String, Object> featureData = new HashMap<>();
            featureData.put("id", request.getFeatureId());
            featureData.put("value", request.getValue());
            
            if (!request.getProductId().isEmpty()) {
                featureData.put("productId", request.getProductId());
            }
            
            if (!request.getCategoryId().isEmpty()) {
                featureData.put("categoryId", request.getCategoryId());
            }
            
            // Use the validation service to validate the feature
            ValidationResult result = validationService.validateProductFeature(featureData);
            
            // Convert to gRPC response
            ValidationResponse.Builder responseBuilder = ValidationResponse.newBuilder()
                    .setValid(result.isValid());
            
            // Add errors
            if (result.getErrors() != null) {
                for (ValidationError error : result.getErrors()) {
                    com.scaler.grpc.validation.ValidationError grpcError = com.scaler.grpc.validation.ValidationError.newBuilder()
                            .setCode(error.getCode() != null ? error.getCode() : "UNKNOWN")
                            .setMessage(error.getMessage() != null ? error.getMessage() : "")
                            .setField(error.getField() != null ? error.getField() : "")
                            .setValue(error.getInvalidValue() != null ? error.getInvalidValue().toString() : "")
                            .build();
                    responseBuilder.addErrors(grpcError);
                }
            }
            
            // Add warnings
            if (result.getWarnings() != null) {
                for (ValidationWarning warning : result.getWarnings()) {
                    com.scaler.grpc.validation.ValidationWarning grpcWarning = com.scaler.grpc.validation.ValidationWarning.newBuilder()
                            .setCode(warning.getCode() != null ? warning.getCode() : "UNKNOWN")
                            .setMessage(warning.getMessage() != null ? warning.getMessage() : "")
                            .setField(warning.getField() != null ? warning.getField() : "")
                            .setValue(warning.getValue() != null ? warning.getValue().toString() : "")
                            .build();
                    responseBuilder.addWarnings(grpcWarning);
                }
            }
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            log.error("Error validating feature value via gRPC", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error validating feature value: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}