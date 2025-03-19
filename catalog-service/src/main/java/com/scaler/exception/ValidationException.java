package com.scaler.exception;

import java.util.List;

/**
 * Exception thrown when validation fails
 */
public class ValidationException extends BaseServiceException {
    
    private static final String DEFAULT_ERROR_CODE = "VALIDATION.ERROR";
    
    public ValidationException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
    
    public ValidationException(String message, String errorCode, Object... parameters) {
        super(message, errorCode, parameters);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause, DEFAULT_ERROR_CODE);
    }
    
    public ValidationException(String message, Throwable cause, String errorCode, Object... parameters) {
        super(message, cause, errorCode, parameters);
    }

    public ValidationException(String validationFailed, List<String> errors) {
        super(validationFailed, DEFAULT_ERROR_CODE, errors.toArray());
    }
}
