package com.nosql.poc.vendor.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Custom error decoder for Feign clients to handle HTTP errors.
 */
public class VendorServiceErrorDecoder implements ErrorDecoder {
    private static final Logger logger = LoggerFactory.getLogger(VendorServiceErrorDecoder.class);
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.status());
        
        if (statusCode.is5xxServerError()) {
            logger.error("Server error during Feign call to {}: HTTP {}", methodKey, response.status());
            return new ServiceUnavailableException("Service unavailable", response.status());
        } else if (statusCode.is4xxClientError()) {
            logger.warn("Client error during Feign call to {}: HTTP {}", methodKey, response.status());
            
            if (statusCode == HttpStatus.NOT_FOUND) {
                return new ResourceNotFoundException("Resource not found", response.status());
            }
            
            return new ClientRequestException("Client error", response.status());
        }
        
        // For other errors, use the default decoder
        return defaultErrorDecoder.decode(methodKey, response);
    }
    
    /**
     * Exception for service unavailable errors.
     */
    public static class ServiceUnavailableException extends RuntimeException {
        private final int status;
        
        public ServiceUnavailableException(String message, int status) {
            super(message);
            this.status = status;
        }
        
        public int getStatus() {
            return status;
        }
    }
    
    /**
     * Exception for client request errors.
     */
    public static class ClientRequestException extends RuntimeException {
        private final int status;
        
        public ClientRequestException(String message, int status) {
            super(message);
            this.status = status;
        }
        
        public int getStatus() {
            return status;
        }
    }
    
    /**
     * Exception for resource not found errors.
     */
    public static class ResourceNotFoundException extends RuntimeException {
        private final int status;
        
        public ResourceNotFoundException(String message, int status) {
            super(message);
            this.status = status;
        }
        
        public int getStatus() {
            return status;
        }
    }
}