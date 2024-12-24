package com.scaler.enums;

/**
 * Enum representing the different types of feature values supported by the system.
 */
public enum FeatureValueType {
    /**
     * Single string value
     */
    STRING,

    /**
     * List of string values
     */
    STRING_LIST,

    /**
     * Single numeric value
     */
    NUMERIC,

    /**
     * List of numeric values
     */
    NUMERIC_LIST,

    /**
     * Single boolean value
     */
    BOOLEAN,

    /**
     * List of boolean values
     */
    BOOLEAN_LIST,

    /**
     * Single reference value
     */
    REFERENCE,

    /**
     * List of reference values
     */
    REFERENCE_LIST;

    /**
     * Checks if this type is a list type
     * @return true if this is a list type, false otherwise
     */
    public boolean isList() {
        return this == STRING_LIST || this == NUMERIC_LIST || 
               this == BOOLEAN_LIST || this == REFERENCE_LIST;
    }

    /**
     * Gets the base type without the list modifier
     * @return the base type
     */
    public FeatureValueType getBaseType() {
        return switch (this) {
            case STRING_LIST -> STRING;
            case NUMERIC_LIST -> NUMERIC;
            case BOOLEAN_LIST -> BOOLEAN;
            case REFERENCE_LIST -> REFERENCE;
            default -> this;
        };
    }

    /**
     * Gets the list version of this type
     * @return the list version of this type
     */
    public FeatureValueType getListType() {
        return switch (this) {
            case STRING -> STRING_LIST;
            case NUMERIC -> NUMERIC_LIST;
            case BOOLEAN -> BOOLEAN_LIST;
            case REFERENCE -> REFERENCE_LIST;
            default -> this;
        };
    }
}
