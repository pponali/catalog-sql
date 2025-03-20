package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for Rule entities from the Rules Service
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RuleDTO extends BaseDTO {
    
    /**
     * Name of the rule
     */
    private String name;
    
    /**
     * Description of what the rule does
     */
    private String description;
    
    /**
     * Type of rule (e.g., "validation", "pricing", "inventory", etc.)
     */
    private String ruleType;
    
    /**
     * The actual rule content (could be a DSL, JSON configuration, etc.)
     */
    private String ruleContent;
    
    /**
     * Priority of the rule for execution order (lower numbers execute first)
     */
    private int priority;
    
    /**
     * Whether the rule is currently active
     */
    private boolean active;
    
    /**
     * IDs of categories this rule applies to
     */
    @Builder.Default
    private List<UUID> categoryIds = new ArrayList<>();
}