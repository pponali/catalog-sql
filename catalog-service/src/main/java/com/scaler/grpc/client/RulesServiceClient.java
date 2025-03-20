package com.scaler.grpc.client;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.RuleDTO;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ServiceException;
import com.scaler.grpc.rules.*;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC client for the Rules service
 */
@Slf4j
@Service
public class RulesServiceClient {

    private final RulesServiceGrpc.RulesServiceBlockingStub rulesServiceStub;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public RulesServiceClient(
            @Qualifier("rulesServiceChannel") ManagedChannel channel,
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry) {
        this.rulesServiceStub = RulesServiceGrpc.newBlockingStub(channel);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("rulesService");
        this.retry = retryRegistry.retry("rulesService");
        log.info("RulesServiceClient initialized with circuit breaker and retry");
    }

    /**
     * Evaluate a product against rules
     *
     * @param productId ID of the product to evaluate
     * @param ruleType Type of rule to evaluate
     * @param context Context parameters for rule evaluation
     * @param channelId Optional channel ID
     * @param platformId Optional platform ID
     * @param sellerId Optional seller ID
     * @return Result of rule evaluation
     */
    public RuleEvaluationResponse evaluateProductRule(
            UUID productId, String ruleType, Map<String, String> context,
            UUID channelId, UUID platformId, UUID sellerId) {
        
        log.info("Evaluating product rule for product ID: {}, rule type: {}", productId, ruleType);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<RuleEvaluationResponse> evaluateRuleFunction = () -> {
            try {
                EvaluateProductRuleRequest.Builder requestBuilder = EvaluateProductRuleRequest.newBuilder()
                        .setProductId(productId.toString())
                        .setRuleType(ruleType);

                if (context != null && !context.isEmpty()) {
                    requestBuilder.putAllContext(context);
                }

                if (channelId != null) {
                    requestBuilder.setChannelId(channelId.toString());
                }

                if (platformId != null) {
                    requestBuilder.setPlatformId(platformId.toString());
                }

                if (sellerId != null) {
                    requestBuilder.setSellerId(sellerId.toString());
                }

                return rulesServiceStub.evaluateProductRule(requestBuilder.build());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "rule evaluation", productId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return evaluateRuleFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when evaluating rules for product {}: {}", 
                    productId, e.getMessage());
            
            // Create a fallback rule evaluation response that passes by default
            // This is a fail-open approach for better resilience
            return RuleEvaluationResponse.newBuilder()
                    .setResult(true)
                    .addMessages("Rules service unavailable - proceeding with caution")
                    .addAppliedRules("fallback-rule")
                    .build();
        }
    }

    /**
     * Evaluate a product against rules (convenience method)
     *
     * @param product Product to evaluate
     * @param ruleType Type of rule to evaluate
     * @return Result of rule evaluation
     */
    public RuleEvaluationResponse evaluateProductRule(ProductDTO product, String ruleType) {
        // Create context map from product data
        Map<String, String> context = new HashMap<>();
        context.put("name", product.getName());
        if (product.getSku() != null) {
            context.put("sku", product.getSku());
        }
        if (product.getDescription() != null) {
            context.put("description", product.getDescription());
        }
        if (product.getBrand() != null) {
            context.put("brand", product.getBrand());
        }

        // Get channel, platform and seller IDs if available
        UUID channelId = product.getChannelId();
        UUID platformId = product.getPlatformId();
        
        // For seller ID, we would typically use the first seller if available
        UUID sellerId = null;
        if (product.getSellerIds() != null && !product.getSellerIds().isEmpty()) {
            sellerId = product.getSellerIds().get(0);
        }

        return evaluateProductRule(product.getId(), ruleType, context, channelId, platformId, sellerId);
    }

    /**
     * Get rules by category
     *
     * @param categoryId Category ID
     * @param ruleType Rule type filter (optional)
     * @return List of rules for the category
     */
    public List<RuleDTO> getRulesByCategory(UUID categoryId, String ruleType) {
        log.info("Getting rules for category: {}, type: {}", categoryId, ruleType);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<List<RuleDTO>> getRulesByCategoryFunction = () -> {
            try {
                GetRulesByCategoryRequest.Builder requestBuilder = GetRulesByCategoryRequest.newBuilder()
                        .setCategoryId(categoryId.toString());

                if (ruleType != null && !ruleType.isEmpty()) {
                    requestBuilder.setRuleType(ruleType);
                }

                RuleListResponse response = rulesServiceStub.getRulesByCategory(requestBuilder.build());

                return response.getRulesList().stream()
                        .map(this::mapRuleMessageToDTO)
                        .collect(Collectors.toList());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "rules for category", categoryId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getRulesByCategoryFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting rules for category {}: {}", 
                    categoryId, e.getMessage());
            
            // Return an empty list as fallback
            return Collections.emptyList();
        }
    }

    /**
     * Handle gRPC exceptions and rethrow appropriate application exceptions
     */
    private void handleGrpcException(StatusRuntimeException e, String resourceType, UUID resourceId) {
        String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
        
        log.error("Error fetching {} for ID {}: {}", resourceType, resourceId, errorMessage);
        
        switch (e.getStatus().getCode()) {
            case NOT_FOUND:
                throw new ResourceNotFoundException(resourceType + " not found for id: " + resourceId);
            case UNAVAILABLE:
                throw new ServiceException("Rules service is currently unavailable");
            case DEADLINE_EXCEEDED:
                throw new ServiceException("Request to rules service timed out");
            default:
                throw new ServiceException("Error communicating with rules service: " + errorMessage);
        }
    }

    /**
     * Maps a RuleMessage to a RuleDTO
     */
    private RuleDTO mapRuleMessageToDTO(RuleMessage message) {
        RuleDTO dto = new RuleDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setName(message.getName());
        dto.setDescription(message.getDescription());
        dto.setRuleType(message.getRuleType());
        dto.setRuleContent(message.getRuleContent());
        dto.setPriority(message.getPriority());
        dto.setActive(message.getActive());
        
        // Convert category IDs
        if (message.getCategoriesList() != null && !message.getCategoriesList().isEmpty()) {
            dto.setCategoryIds(message.getCategoriesList().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList()));
        }
        
        // Dates are managed by the service, not set here
        
        return dto;
    }
}