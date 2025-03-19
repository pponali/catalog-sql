package com.scaler.exception;

/**
 * Exception thrown when a service operation fails due to an unexpected error.
 */
public class ServiceException extends BaseServiceException {
    
    private static final String DEFAULT_ERROR_CODE = "SERVICE.INTERNAL_ERROR";
    
    public ServiceException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
    
    public ServiceException(String message, String errorCode, Object... parameters) {
        super(message, errorCode, parameters);
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause, DEFAULT_ERROR_CODE);
    }
    
    public ServiceException(String message, Throwable cause, String errorCode, Object... parameters) {
        super(message, cause, errorCode, parameters);
    }
}
