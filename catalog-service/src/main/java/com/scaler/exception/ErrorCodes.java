package com.scaler.exception;

/**
 * Centralized error codes for the application
 */
public final class ErrorCodes {
    
    private ErrorCodes() {
        // Private constructor to prevent instantiation
    }
    
    // Validation error codes
    public static final String VALIDATION_ERROR = "VALIDATION.ERROR";
    public static final String VALIDATION_NULL = "VALIDATION.NULL";
    public static final String VALIDATION_EMPTY = "VALIDATION.EMPTY";
    public static final String VALIDATION_LENGTH = "VALIDATION.LENGTH";
    public static final String VALIDATION_RANGE = "VALIDATION.RANGE";
    public static final String VALIDATION_FORMAT = "VALIDATION.FORMAT";
    
    // Resource error codes
    public static final String RESOURCE_NOT_FOUND = "RESOURCE.NOT_FOUND";
    public static final String RESOURCE_DUPLICATE = "RESOURCE.DUPLICATE";
    public static final String RESOURCE_INVALID_STATE = "RESOURCE.INVALID_STATE";
    
    // Service error codes
    public static final String SERVICE_ERROR = "SERVICE.ERROR";
    public static final String SERVICE_UNAVAILABLE = "SERVICE.UNAVAILABLE";
    public static final String SERVICE_TIMEOUT = "SERVICE.TIMEOUT";
    
    // Data error codes
    public static final String DATA_INTEGRITY = "DATA.INTEGRITY";
    public static final String DATA_STATE = "DATA.STATE";
    public static final String DATA_REFERENCE = "DATA.REFERENCE";
    
    // Security error codes
    public static final String SECURITY_UNAUTHORIZED = "SECURITY.UNAUTHORIZED";
    public static final String SECURITY_FORBIDDEN = "SECURITY.FORBIDDEN";
    public static final String SECURITY_INVALID_TOKEN = "SECURITY.INVALID_TOKEN";
}
