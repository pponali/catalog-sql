package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines the strategy for applying validation rules.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationStrategy {
    
    /**
     * The type of strategy to apply for validations.
     */
    private StrategyType type;
    
    /**
     * The threshold for validation errors (used with some strategy types).
     */
    private Integer errorThreshold;
    
    /**
     * Whether to fail fast on first error or collect all errors.
     */
    private boolean failFast;
    
    /**
     * The severity level that causes a validation to fail.
     */
    private String failOnSeverity;
    
    /**
     * Types of validation strategies.
     */
    public enum StrategyType {
        /**
         * All rules must pass.
         */
        ALL_MUST_PASS,
        
        /**
         * At least one rule must pass.
         */
        ANY_MUST_PASS,
        
        /**
         * A percentage of rules must pass.
         */
        PERCENTAGE_MUST_PASS,
        
        /**
         * Rules are weighted, and total score must be above threshold.
         */
        WEIGHTED
    }
}