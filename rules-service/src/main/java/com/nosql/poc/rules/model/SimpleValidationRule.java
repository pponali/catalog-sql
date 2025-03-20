package com.nosql.poc.rules.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Simplified validation rule model for CSV imports
 */
@Data
@Document(collection = "validation_rules")
public class SimpleValidationRule {
    @Id
    private String id;
    private String ruleId;
    private String name;
    private String description;
    private String entityType; // PRODUCT, PARTNER, CATEGORY, etc.
    private String attribute; // The attribute this rule validates
    private String condition; // The validation condition (e.g., NOT_NULL, REGEX, MIN_LENGTH, etc.)
    private String conditionValue; // The value used in the condition (e.g., regex pattern, min length, etc.)
    private String message; // The message to display if validation fails
    private String severity; // ERROR, WARNING, INFO
    private Integer priority; // Higher numbers = higher priority
    private Boolean active;
    private String category; // Business category this rule applies to (ELECTRONICS, FASHION, etc.)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter and setter methods are provided by Lombok @Data
}