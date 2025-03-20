package com.nosql.poc.rules.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Simplified business rule model for CSV imports
 */
@Data
@Document(collection = "business_rules")
public class SimpleBusinessRule {
    @Id
    private String id;
    private String ruleId;
    private String name;
    private String description;
    private String ruleType; // PRICING, DISCOUNT, PROMOTION, SHIPPING, etc.
    private String entityType; // PRODUCT, ORDER, CUSTOMER, etc.
    private String condition; // The condition expression
    private String action; // The action to perform when the condition is met
    private String actionParameters; // JSON parameters for the action
    private String priority; // Rule priority (higher number = higher priority)
    private Boolean active;
    private String category; // Business category this rule applies to
    private LocalDateTime startDate; // When the rule becomes active
    private LocalDateTime endDate; // When the rule expires
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter and setter methods are provided by Lombok @Data
}