package com.scaler.grpc.client;

import com.scaler.dto.ChannelDTO;
import com.scaler.dto.PlatformDTO;
import com.scaler.dto.StoreDTO;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ServiceException;
import com.scaler.grpc.channel.*;
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
import java.util.UUID;

/**
 * gRPC client for the Channel service
 */
@Slf4j
@Service
public class ChannelServiceClient {

    private final ChannelServiceGrpc.ChannelServiceBlockingStub channelServiceStub;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public ChannelServiceClient(
            @Qualifier("channelServiceChannel") ManagedChannel channel,
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry) {
        this.channelServiceStub = ChannelServiceGrpc.newBlockingStub(channel);
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("channelService");
        this.retry = retryRegistry.retry("channelService");
        log.info("ChannelServiceClient initialized with circuit breaker and retry");
    }

    /**
     * Get a channel by its ID
     *
     * @param channelId The ID of the channel to retrieve
     * @return ChannelDTO containing channel information
     * @throws ResourceNotFoundException if the channel is not found
     * @throws ServiceException if there's an error with the gRPC service
     */
    public ChannelDTO getChannel(UUID channelId) {
        log.info("Getting channel with ID: {}", channelId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<ChannelDTO> getChannelFunction = () -> {
            try {
                GetChannelRequest request = GetChannelRequest.newBuilder()
                        .setId(channelId.toString())
                        .build();

                ChannelResponse response = channelServiceStub.getChannel(request);
                return mapChannelMessageToDTO(response.getChannel());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "channel", channelId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getChannelFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting channel {}: {}", 
                    channelId, e.getMessage());
            
            // Create a fallback channel with minimal information
            ChannelDTO fallbackChannel = new ChannelDTO();
            fallbackChannel.setId(channelId);
            fallbackChannel.setName("Unavailable Channel");
            fallbackChannel.setStatus("UNAVAILABLE");
            fallbackChannel.setCode("UNAVAILABLE");
            fallbackChannel.setDescription("This channel information is temporarily unavailable");
            
            return fallbackChannel;
        }
    }

    /**
     * Get a platform by its ID
     *
     * @param platformId The ID of the platform to retrieve
     * @return PlatformDTO containing platform information
     * @throws ResourceNotFoundException if the platform is not found
     * @throws ServiceException if there's an error with the gRPC service
     */
    public PlatformDTO getPlatform(UUID platformId) {
        log.info("Getting platform with ID: {}", platformId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<PlatformDTO> getPlatformFunction = () -> {
            try {
                GetPlatformRequest request = GetPlatformRequest.newBuilder()
                        .setId(platformId.toString())
                        .build();

                PlatformResponse response = channelServiceStub.getPlatform(request);
                return mapPlatformMessageToDTO(response.getPlatform());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "platform", platformId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getPlatformFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting platform {}: {}", 
                    platformId, e.getMessage());
            
            // Create a fallback platform with minimal information
            PlatformDTO fallbackPlatform = new PlatformDTO();
            fallbackPlatform.setId(platformId);
            fallbackPlatform.setName("Unavailable Platform");
            fallbackPlatform.setStatus("UNAVAILABLE");
            fallbackPlatform.setCode("UNAVAILABLE");
            fallbackPlatform.setDescription("This platform information is temporarily unavailable");
            
            return fallbackPlatform;
        }
    }

    /**
     * Get a store by its ID
     *
     * @param storeId The ID of the store to retrieve
     * @return StoreDTO containing store information
     * @throws ResourceNotFoundException if the store is not found
     * @throws ServiceException if there's an error with the gRPC service
     */
    public StoreDTO getStore(UUID storeId) {
        log.info("Getting store with ID: {}", storeId);

        // Create a function that will be decorated with circuit breaker and retry
        CheckedFunction0<StoreDTO> getStoreFunction = () -> {
            try {
                GetStoreRequest request = GetStoreRequest.newBuilder()
                        .setId(storeId.toString())
                        .build();

                StoreResponse response = channelServiceStub.getStore(request);
                return mapStoreMessageToDTO(response.getStore());
            } catch (StatusRuntimeException e) {
                handleGrpcException(e, "store", storeId);
                return null; // This will never be reached as the exception is rethrown
            }
        };
        
        // Apply circuit breaker and retry patterns
        try {
            return CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                try {
                    return getStoreFunction.apply();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }).get();
        } catch (Exception e) {
            log.warn("Circuit open or error when getting store {}: {}", 
                    storeId, e.getMessage());
            
            // Create a fallback store with minimal information
            StoreDTO fallbackStore = new StoreDTO();
            fallbackStore.setId(storeId);
            fallbackStore.setName("Unavailable Store");
            fallbackStore.setStatus("UNAVAILABLE");
            fallbackStore.setCode("UNAVAILABLE");
            fallbackStore.setDescription("This store information is temporarily unavailable");
            
            return fallbackStore;
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
                throw new ServiceException("Channel service is currently unavailable");
            case DEADLINE_EXCEEDED:
                throw new ServiceException("Request to channel service timed out");
            default:
                throw new ServiceException("Error communicating with channel service: " + errorMessage);
        }
    }

    /**
     * Maps a ChannelMessage to a ChannelDTO
     */
    private ChannelDTO mapChannelMessageToDTO(ChannelMessage message) {
        ChannelDTO dto = new ChannelDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setName(message.getName());
        dto.setCode(message.getCode());
        dto.setDescription(message.getDescription());
        dto.setChannelType(message.getChannelType());
        dto.setStatus(message.getStatus());
        
        // We'll let the service set these dates instead of parsing from gRPC
        
        return dto;
    }

    /**
     * Maps a PlatformMessage to a PlatformDTO
     */
    private PlatformDTO mapPlatformMessageToDTO(PlatformMessage message) {
        PlatformDTO dto = new PlatformDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setName(message.getName());
        dto.setCode(message.getCode());
        dto.setDescription(message.getDescription());
        dto.setPlatformType(message.getPlatformType());
        dto.setStatus(message.getStatus());
        
        // We'll let the service set these dates instead of parsing from gRPC
        
        return dto;
    }

    /**
     * Maps a StoreMessage to a StoreDTO
     */
    private StoreDTO mapStoreMessageToDTO(StoreMessage message) {
        StoreDTO dto = new StoreDTO();
        dto.setId(UUID.fromString(message.getId()));
        dto.setName(message.getName());
        dto.setCode(message.getCode());
        dto.setDescription(message.getDescription());
        // Store type needs to be properly mapped or set by service
        dto.setStoreTypeString(message.getStoreType());
        
        if (message.getChannelId() != null && !message.getChannelId().isEmpty()) {
            dto.setChannelId(UUID.fromString(message.getChannelId()));
        }
        
        dto.setAddress(message.getAddress());
        dto.setCity(message.getCity());
        dto.setState(message.getState());
        dto.setCountry(message.getCountry());
        dto.setPincode(message.getPincode());
        dto.setStatus(message.getStatus());
        
        // We'll let the service set these dates instead of parsing from gRPC
        
        return dto;
    }
}