package com.scaler.util;

import com.scaler.exception.ValidationException;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Utility class for common validation operations
 */
public final class ValidationUtils {
    
    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }
    
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(String.format("%s cannot be null", fieldName));
        }
    }
    
    public static void validateNotEmpty(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new ValidationException(String.format("%s cannot be empty", fieldName));
        }
    }
    
    public static void validateNotEmpty(Collection<?> collection, String fieldName) {
        if (collection == null || collection.isEmpty()) {
            throw new ValidationException(String.format("%s cannot be empty", fieldName));
        }
    }
    
    public static void validatePositive(Number value, String fieldName) {
        if (value == null || value.doubleValue() <= 0) {
            throw new ValidationException(String.format("%s must be positive", fieldName));
        }
    }
    
    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new ValidationException(
                String.format("%s length cannot exceed %d characters", fieldName, maxLength));
        }
    }
}
