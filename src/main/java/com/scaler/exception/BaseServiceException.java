package com.scaler.exception;

import lombok.Getter;

/**
 * Base exception class for all service layer exceptions
 */
@Getter
public abstract class BaseServiceException extends RuntimeException {
    
    private final String errorCode;
    private final transient Object[] parameters;
    
    protected BaseServiceException(String message, String errorCode, Object... parameters) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }
    
    protected BaseServiceException(String message, Throwable cause, String errorCode, Object... parameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }
}
