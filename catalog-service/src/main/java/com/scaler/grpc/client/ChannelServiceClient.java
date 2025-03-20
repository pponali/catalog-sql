package com.scaler.grpc.client;

import com.scaler.dto.ChannelDTO;
import com.scaler.dto.PlatformDTO;
import com.scaler.entity.enums.ChannelType;
import com.scaler.entity.enums.PlatformType;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ServiceException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Stub implementation of Channel service client for the microservice separation
 * This provides fallback responses when the actual Channel service is not available
 */
@Slf4j
@Service
public class ChannelServiceClient {

    private final CircuitBreaker circuitBreaker;

    public ChannelServiceClient(
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry) {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("channelService");
        log.info("Simplified ChannelServiceClient initialized with circuit breaker");
    }

    /**
     * Get a channel by its ID - simplified implementation that returns fallback data
     *
     * @param channelId The ID of the channel to retrieve
     * @return ChannelDTO containing channel information
     */
    public ChannelDTO getChannel(UUID channelId) {
        log.info("Getting channel with ID: {} (simplified fallback implementation)", channelId);
        
        // Return a fallback channel with minimal information
        ChannelDTO fallbackChannel = new ChannelDTO();
        fallbackChannel.setId(channelId);
        fallbackChannel.setName("Channel " + channelId.toString().substring(0, 8));
        fallbackChannel.setStatus("ACTIVE");
        fallbackChannel.setCode("CHANNEL_" + channelId.toString().substring(0, 4));
        fallbackChannel.setDescription("Fallback channel information provided by catalog service");
        fallbackChannel.setChannelType("WEB");
        fallbackChannel.setType(ChannelType.ECOMMERCE);
        fallbackChannel.setEnabled(true);
        fallbackChannel.setCreatedDate(LocalDateTime.now().toString());
        fallbackChannel.setLastModifiedDate(LocalDateTime.now().toString());
        
        return fallbackChannel;
    }

    /**
     * Get a platform by its ID - simplified implementation that returns fallback data
     *
     * @param platformId The ID of the platform to retrieve
     * @return PlatformDTO containing platform information
     */
    public PlatformDTO getPlatform(UUID platformId) {
        log.info("Getting platform with ID: {} (simplified fallback implementation)", platformId);
        
        // Return a fallback platform with minimal information
        PlatformDTO fallbackPlatform = new PlatformDTO();
        fallbackPlatform.setId(platformId);
        fallbackPlatform.setName("Platform " + platformId.toString().substring(0, 8));
        fallbackPlatform.setStatus("ACTIVE");
        fallbackPlatform.setCode("PLATFORM_" + platformId.toString().substring(0, 4));
        fallbackPlatform.setDescription("Fallback platform information provided by catalog service");
        fallbackPlatform.setPlatformType("WEB");
        fallbackPlatform.setType(PlatformType.DESKTOP_WEB);
        fallbackPlatform.setEnabled(true);
        fallbackPlatform.setCreatedDate(LocalDateTime.now().toString());
        fallbackPlatform.setLastModifiedDate(LocalDateTime.now().toString());
        
        return fallbackPlatform;
    }
}