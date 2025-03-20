package com.nosql.poc.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information message from validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationInfo {
    private String rule;
    private String message;
}