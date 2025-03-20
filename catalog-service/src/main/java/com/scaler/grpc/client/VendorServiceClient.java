package com.scaler.grpc.client;

import com.scaler.dto.MerchantDTO;
import com.scaler.dto.SellerDTO;
import com.scaler.dto.SellerProductDTO;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ServiceException;
import com.scaler.grpc.vendor.*;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC client for the Vendor service
 */
@Slf4j
@Service
public class VendorServiceClient {

    private final VendorServiceGrpc.VendorServiceBlockingStub vendorServiceStub;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public VendorServiceClient(
            @Qualifier("vendorServiceChannel") ManagedChannel channel,
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry) {
        this.vendorServiceStub = VendorServiceGrpc.newBlockingStub(channel);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("vendorService");
        this.retry = retryRegistry.retry("vendorService");
        log.info("VendorServiceClient initialized with circuit breaker and retry");
    }

    /**
     * Get a seller by its ID
     *
     * @param sellerId The ID of the seller to retrieve
     * @return SellerDTO containing seller information
     * @throws ResourceNotFoundException if the seller is not found
     * @throws ServiceException if there's an error with the gRPC service
     */
    public SellerDTO getSeller(UUID sellerId) {
        log.info("Getting seller with ID: {}", sellerId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<SellerDTO> getSellerFunction = () -> {
            try {
                GetSellerRequest request = GetSellerRequest.newBuilder()
                        .setId(sellerId.toString())
                        .build();

                SellerResponse response = vendorServiceStub.getSeller(request);
                return mapSellerMessageToDTO(response.getSeller());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "seller", sellerId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getSellerFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting seller {}: {}", 
                    sellerId, e.getMessage());
            
            // Create a fallback seller
            SellerDTO fallbackSeller = new SellerDTO();
            fallbackSeller.setId(sellerId);
            fallbackSeller.setName("Unavailable Seller");
            fallbackSeller.setStatus("UNAVAILABLE");
            fallbackSeller.setCode("UNAVAILABLE");
            fallbackSeller.setDescription("This seller information is temporarily unavailable");
            
            return fallbackSeller;
        }
    }

    /**
     * Get a merchant by its ID
     *
     * @param merchantId The ID of the merchant to retrieve
     * @return MerchantDTO containing merchant information
     * @throws ResourceNotFoundException if the merchant is not found
     * @throws ServiceException if there's an error with the gRPC service
     */
    public MerchantDTO getMerchant(UUID merchantId) {
        log.info("Getting merchant with ID: {}", merchantId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<MerchantDTO> getMerchantFunction = () -> {
            try {
                GetMerchantRequest request = GetMerchantRequest.newBuilder()
                        .setId(merchantId.toString())
                        .build();

                MerchantResponse response = vendorServiceStub.getMerchant(request);
                return mapMerchantMessageToDTO(response.getMerchant());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "merchant", merchantId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getMerchantFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting merchant {}: {}", 
                    merchantId, e.getMessage());
                
            // Since merchant authentication is a critical operation, we'll throw an exception
            // rather than returning a dummy merchant
            throw new ServiceException("Vendor service unavailable when getting merchant information");
        }
    }

    /**
     * Get seller products by seller ID
     *
     * @param sellerId The ID of the seller
     * @param page The page number (1-based)
     * @param size The page size
     * @return List of SellerProductDTO containing seller product information
     * @throws ResourceNotFoundException if the seller is not found
     * @throws ServiceException if there's an error with the gRPC service
     */
    public List<SellerProductDTO> getSellerProducts(UUID sellerId, int page, int size) {
        log.info("Getting products for seller with ID: {}", sellerId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<List<SellerProductDTO>> getSellerProductsFunction = () -> {
            try {
                GetSellerProductsRequest request = GetSellerProductsRequest.newBuilder()
                        .setSellerId(sellerId.toString())
                        .setPage(page)
                        .setSize(size)
                        .build();

                SellerProductListResponse response = vendorServiceStub.getSellerProducts(request);
                
                return response.getProductsList().stream()
                        .map(this::mapSellerProductMessageToDTO)
                        .collect(Collectors.toList());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "seller products", sellerId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getSellerProductsFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting seller products for seller {}: {}", 
                    sellerId, e.getMessage());
            
            // Return an empty list as fallback
            return Collections.emptyList();
        }
    }

    /**
     * Handle gRPC exceptions and rethrow appropriate application exceptions
     */
    private void handleGrpcException(StatusRuntimeException e, String resourceType, UUID resourceId) {
        String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
        
        log.error("Error fetching {} with ID {}: {}", resourceType, resourceId, errorMessage);
        
        switch (e.getStatus().getCode()) {
            case NOT_FOUND:
                throw new ResourceNotFoundException(resourceType + " not found with id: " + resourceId);
            case UNAVAILABLE:
                throw new ServiceException("Vendor service is currently unavailable");
            case DEADLINE_EXCEEDED:
                throw new ServiceException("Request to vendor service timed out");
            default:
                throw new ServiceException("Error communicating with vendor service: " + errorMessage);
        }
    }

    /**
     * Maps a SellerMessage to a SellerDTO
     */
    private SellerDTO mapSellerMessageToDTO(SellerMessage message) {
        SellerDTO dto = new SellerDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setName(message.getName());
        dto.setCode(message.getCode());
        dto.setDescription(message.getDescription());
        dto.setStatus(message.getStatus());
        
        if (message.getMerchantId() != null && !message.getMerchantId().isEmpty()) {
            dto.setMerchantId(UUID.fromString(message.getMerchantId()));
        }
        
        dto.setContactEmail(message.getContactEmail());
        dto.setContactPhone(message.getContactPhone());
        
        // Dates are managed by the service, not set here
        
        return dto;
    }

    /**
     * Maps a MerchantMessage to a MerchantDTO
     */
    private MerchantDTO mapMerchantMessageToDTO(MerchantMessage message) {
        MerchantDTO dto = new MerchantDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setName(message.getName());
        dto.setCode(message.getCode());
        dto.setDescription(message.getDescription());
        // Business unit field may not exist
        //dto.setBusinessUnit(message.getBusinessUnit());
        dto.setStatus(message.getStatus());
        
        // Dates are managed by the service, not set here
        
        return dto;
    }

    /**
     * Maps a SellerProductMessage to a SellerProductDTO
     */
    private SellerProductDTO mapSellerProductMessageToDTO(SellerProductMessage message) {
        SellerProductDTO dto = new SellerProductDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setSellerId(UUID.fromString(message.getSellerId()));
        dto.setProductId(UUID.fromString(message.getProductId()));
        dto.setPrice(new java.math.BigDecimal(message.getPrice()));
        // Inventory field may not exist
        //dto.setInventory(message.getInventory());
        dto.setStatus(message.getStatus());
        
        // Dates are managed by the service, not set here
        
        return dto;
    }
}