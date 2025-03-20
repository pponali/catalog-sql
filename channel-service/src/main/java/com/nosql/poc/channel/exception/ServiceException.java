package com.nosql.poc.channel.exception;

import lombok.Getter;

/**
 * Exception thrown when a service-level error occurs.
 */
@Getter
public class ServiceException extends RuntimeException {
    
    /**
     * The HTTP status code.
     */
    private final int statusCode;
    
    /**
     * The error code.
     */
    private final String errorCode;
    
    /**
     * Creates a new ServiceException.
     * 
     * @param message the error message
     * @param statusCode the HTTP status code
     * @param errorCode the error code
     */
    public ServiceException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
    /**
     * Creates a new ServiceException.
     * 
     * @param message the error message
     * @param cause the cause
     * @param statusCode the HTTP status code
     * @param errorCode the error code
     */
    public ServiceException(String message, Throwable cause, int statusCode, String errorCode) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}