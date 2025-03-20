package com.scaler.grpc.client;

import com.scaler.dto.CategoryDTO;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ServiceException;
import com.scaler.grpc.partner.*;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC client for the Partner service
 */
@Slf4j
@Service
public class PartnerServiceClient {

    private final PartnerServiceGrpc.PartnerServiceBlockingStub partnerServiceStub;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public PartnerServiceClient(
            @Qualifier("partnerServiceChannel") ManagedChannel channel,
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry) {
        this.partnerServiceStub = PartnerServiceGrpc.newBlockingStub(channel);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("partnerService");
        this.retry = retryRegistry.retry("partnerService");
        log.info("PartnerServiceClient initialized with circuit breaker and retry");
    }

    /**
     * Get a category by ID
     *
     * @param categoryId The ID of the category
     * @return CategoryDTO containing category details
     * @throws ResourceNotFoundException if the category is not found
     * @throws ServiceException if there's an error communicating with the partner service
     */
    public CategoryDTO getCategory(UUID categoryId) {
        log.info("Getting category with ID: {}", categoryId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<CategoryDTO> getCategoryFunction = () -> {
            try {
                GetCategoryRequest request = GetCategoryRequest.newBuilder()
                        .setId(categoryId.toString())
                        .build();

                CategoryResponse response = partnerServiceStub.getCategory(request);
                
                if (response == null || response.getCategory() == null) {
                    throw new ResourceNotFoundException("Category not found with ID: " + categoryId);
                }
                
                return convertToCategoryDTO(response.getCategory());
                
            } catch (StatusRuntimeException e) {
                log.error("Error getting category: {} - {}", categoryId, e.getMessage());
                throw new ServiceException("Failed to get category: " + e.getMessage());
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getCategoryFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting category {}: {}", 
                    categoryId, e.getMessage());
            throw new ServiceException("Service unavailable when getting category: " + e.getMessage());
        }
    }

    /**
     * Get a category by code
     *
     * @param categoryCode The code of the category
     * @return CategoryDTO containing category details
     * @throws ResourceNotFoundException if the category is not found
     * @throws ServiceException if there's an error communicating with the partner service
     */
    public CategoryDTO getCategoryByCode(String categoryCode) {
        log.info("Getting category with code: {}", categoryCode);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<CategoryDTO> getCategoryByCodeFunction = () -> {
            try {
                GetCategoryByCodeRequest request = GetCategoryByCodeRequest.newBuilder()
                        .setCode(categoryCode)
                        .build();

                CategoryResponse response = partnerServiceStub.getCategoryByCode(request);
                
                if (response == null || response.getCategory() == null) {
                    throw new ResourceNotFoundException("Category not found with code: " + categoryCode);
                }
                
                return convertToCategoryDTO(response.getCategory());
                
            } catch (StatusRuntimeException e) {
                log.error("Error getting category by code: {} - {}", categoryCode, e.getMessage());
                throw new ServiceException("Failed to get category by code: " + e.getMessage());
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getCategoryByCodeFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting category by code {}: {}", 
                    categoryCode, e.getMessage());
            throw new ServiceException("Service unavailable when getting category by code: " + e.getMessage());
        }
    }

    /**
     * Get categories by parent ID
     *
     * @param parentId The ID of the parent category
     * @return List of CategoryDTOs containing child categories
     * @throws ServiceException if there's an error communicating with the partner service
     */
    public List<CategoryDTO> getCategoriesByParent(UUID parentId) {
        log.info("Getting categories with parent ID: {}", parentId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<List<CategoryDTO>> getCategoriesByParentFunction = () -> {
            try {
                GetCategoriesByParentRequest request = GetCategoriesByParentRequest.newBuilder()
                        .setParentId(parentId.toString())
                        .build();

                CategoryListResponse response = partnerServiceStub.getCategoriesByParent(request);
                
                return response.getCategoriesList().stream()
                        .map(this::convertToCategoryDTO)
                        .collect(Collectors.toList());
                
            } catch (StatusRuntimeException e) {
                log.error("Error getting categories by parent: {} - {}", parentId, e.getMessage());
                throw new ServiceException("Failed to get categories by parent: " + e.getMessage());
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getCategoriesByParentFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting categories by parent {}: {}", 
                    parentId, e.getMessage());
            // Return an empty list as fallback
            return new ArrayList<>();
        }
    }

    /**
     * Validates if a category exists
     *
     * @param categoryId The ID of the category to validate
     * @return true if the category exists, false otherwise
     */
    public boolean validateCategoryExists(UUID categoryId) {
        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<Boolean> validateCategoryFunction = () -> {
            try {
                // Use the already circuit-breaker protected getCategory method
                getCategory(categoryId);
                return true;
            } catch (ResourceNotFoundException e) {
                return false;
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return validateCategoryFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when validating category {}: {}", 
                    categoryId, e.getMessage());
            // In case of service error, we'll still return true to avoid blocking operations
            // This is a fail-open approach for better resilience
            return true;
        }
    }

    /**
     * Converts a CategoryMessage to a CategoryDTO
     */
    private CategoryDTO convertToCategoryDTO(CategoryMessage message) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setName(message.getName());
        dto.setCode(message.getCode());
        dto.setDescription(message.getDescription());
        
        // These fields might not exist in the DTO, adjust as needed
        /*if (message.getCategoryType() != null && !message.getCategoryType().isEmpty()) {
            dto.setCategoryType(message.getCategoryType());
        }*/
        
        if (message.getParentId() != null && !message.getParentId().isEmpty()) {
            dto.setParentId(UUID.fromString(message.getParentId()));
        }
        
        // These fields don't exist in the current DTO
        /*dto.setLevel(message.getLevel());
        dto.setPath(message.getPath());
        dto.setLeaf(message.getLeaf());
        
        if (message.getMerchantId() != null && !message.getMerchantId().isEmpty()) {
            dto.setMerchantId(UUID.fromString(message.getMerchantId()));
        }*/
        
        if (message.getCatalogId() != null && !message.getCatalogId().isEmpty()) {
            dto.setCatalogId(UUID.fromString(message.getCatalogId()));
        }
        
        return dto;
    }
}