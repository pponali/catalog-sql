package com.scaler.entity;

/**
 * Enum representing different types of features in the system.
 * Used to ensure type safety when specifying feature types.
 */
public enum FeatureType {
    SPECIFICATION,  // For technical specifications
    ENUM,          // For enumerated values
    STRING,        // For text values
    NUMBER,        // For numeric values
    BOOLEAN,       // For true/false values
    DATE,          // For date values
    DATETIME,      // For date-time values
    OBJECT,        // For complex/nested values
    ARRAY,        // For list/array values
    DECIMAL
}
