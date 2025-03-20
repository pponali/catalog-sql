package com.nosql.poc.validation.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enhanced validation rule model with support for complex conditions and
 * cross-field validations.
 */
@Data
@Document(collection = "enhanced_validation_rules")
public class EnhancedValidationRule {
    @Id
    private String id;
    private String ruleId;
    private String name;
    private String description;
    private String entityType;
    
    // The attribute this rule validates (can be multiple for cross-field validations)
    private List<String> attributes = new ArrayList<>();
    
    // The primary attribute being validated
    private String primaryAttribute;
    
    // The type of condition
    private RuleConditionType conditionType;
    
    // For simple conditions, this is the value to validate against
    private String conditionValue;
    
    // For complex conditions, this is the expression to evaluate
    private String conditionExpression;
    
    // For conditional rules, these are the if-then-else conditions
    private Map<String, String> conditionalExpressions = new HashMap<>();
    
    // For calculated fields, this is the calculation expression
    private String calculationExpression;
    
    // For cross-field validations, these are the field relationships
    private Map<String, String> fieldRelationships = new HashMap<>();
    
    // The message to display if validation fails
    private String message;
    
    // The severity of the validation failure
    private ValidationSeverity severity = ValidationSeverity.ERROR;
    
    // Higher numbers = higher priority
    private Integer priority = 1;
    
    // Whether the rule is active
    private Boolean active = true;
    
    // Business categories this rule applies to (e.g., ELECTRONICS, FASHION)
    private List<String> categories = new ArrayList<>();
    
    // Dependencies on other rules that must be evaluated first
    private List<String> dependsOn = new ArrayList<>();
    
    // When this rule was created and last updated
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Custom JavaScript validation function (for complex cases)
    private String validationScript;
    
    /**
     * Add a category to this rule.
     * 
     * @param category the category to add
     * @return this rule for chaining
     */
    public EnhancedValidationRule addCategory(String category) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        categories.add(category);
        return this;
    }
    
    /**
     * Add an attribute to this rule.
     * 
     * @param attribute the attribute to add
     * @return this rule for chaining
     */
    public EnhancedValidationRule addAttribute(String attribute) {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        attributes.add(attribute);
        return this;
    }
    
    /**
     * Add a rule dependency.
     * 
     * @param ruleId the ID of the rule this rule depends on
     * @return this rule for chaining
     */
    public EnhancedValidationRule addDependency(String ruleId) {
        if (dependsOn == null) {
            dependsOn = new ArrayList<>();
        }
        dependsOn.add(ruleId);
        return this;
    }
    
    /**
     * Add a conditional expression.
     * 
     * @param condition the condition
     * @param expression the expression to evaluate if condition is true
     * @return this rule for chaining
     */
    public EnhancedValidationRule addConditionalExpression(String condition, String expression) {
        if (conditionalExpressions == null) {
            conditionalExpressions = new HashMap<>();
        }
        conditionalExpressions.put(condition, expression);
        return this;
    }
    
    /**
     * Add a field relationship for cross-field validation.
     * 
     * @param field1 the first field
     * @param field2 the second field
     * @return this rule for chaining
     */
    public EnhancedValidationRule addFieldRelationship(String field1, String field2) {
        if (fieldRelationships == null) {
            fieldRelationships = new HashMap<>();
        }
        fieldRelationships.put(field1, field2);
        return this;
    }
    
    // Manual getters and setters in case Lombok is not working properly
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getRuleId() {
        return ruleId;
    }
    
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getPrimaryAttribute() {
        return primaryAttribute;
    }
    
    public void setPrimaryAttribute(String primaryAttribute) {
        this.primaryAttribute = primaryAttribute;
    }
    
    public RuleConditionType getConditionType() {
        return conditionType;
    }
    
    public void setConditionType(RuleConditionType conditionType) {
        this.conditionType = conditionType;
    }
    
    public String getConditionValue() {
        return conditionValue;
    }
    
    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }
    
    public String getConditionExpression() {
        return conditionExpression;
    }
    
    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }
    
    public String getCalculationExpression() {
        return calculationExpression;
    }
    
    public void setCalculationExpression(String calculationExpression) {
        this.calculationExpression = calculationExpression;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public ValidationSeverity getSeverity() {
        return severity;
    }
    
    public void setSeverity(ValidationSeverity severity) {
        this.severity = severity;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getValidationScript() {
        return validationScript;
    }
    
    public void setValidationScript(String validationScript) {
        this.validationScript = validationScript;
    }
    
    public List<String> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }
    
    public Map<String, String> getConditionalExpressions() {
        return conditionalExpressions;
    }
    
    public void setConditionalExpressions(Map<String, String> conditionalExpressions) {
        this.conditionalExpressions = conditionalExpressions;
    }
    
    public Map<String, String> getFieldRelationships() {
        return fieldRelationships;
    }
    
    public void setFieldRelationships(Map<String, String> fieldRelationships) {
        this.fieldRelationships = fieldRelationships;
    }
    
    public List<String> getCategories() {
        return categories;
    }
    
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    
    public List<String> getDependsOn() {
        return dependsOn;
    }
    
    public void setDependsOn(List<String> dependsOn) {
        this.dependsOn = dependsOn;
    }
}