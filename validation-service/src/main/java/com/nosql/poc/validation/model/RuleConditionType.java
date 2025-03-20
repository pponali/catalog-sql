package com.nosql.poc.validation.model;

/**
 * Enum for rule condition types.
 */
public enum RuleConditionType {
    // Basic conditions
    NOT_NULL,
    NOT_EMPTY,
    REGEX,
    MIN_LENGTH,
    MAX_LENGTH,
    MIN_VALUE,
    MAX_VALUE,
    ENUM,
    
    // Enhanced conditions
    CONDITIONAL, // For if-then-else rules
    CROSS_FIELD, // For comparing fields
    CALCULATED,  // For calculated field validation
    DEPENDENT,   // For dependent field validation
    COMPOSITE    // For composite rules (AND/OR combinations)
}