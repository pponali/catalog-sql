package com.scaler.exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends BaseServiceException {
    
    private static final String DEFAULT_ERROR_CODE = "RESOURCE.NOT_FOUND";
    
    public ResourceNotFoundException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
    
    public ResourceNotFoundException(String message, String errorCode, Object... parameters) {
        super(message, errorCode, parameters);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, DEFAULT_ERROR_CODE);
    }
    
    public ResourceNotFoundException(String message, Throwable cause, String errorCode, Object... parameters) {
        super(message, cause, errorCode, parameters);
    }
}
