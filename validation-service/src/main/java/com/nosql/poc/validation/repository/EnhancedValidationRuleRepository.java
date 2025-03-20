package com.nosql.poc.validation.repository;

import com.nosql.poc.validation.model.EnhancedValidationRule;
import com.nosql.poc.validation.model.ValidationSeverity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Repository for enhanced validation rules.
 */
public interface EnhancedValidationRuleRepository extends MongoRepository<EnhancedValidationRule, String> {
    
    /**
     * Find rules by entity type.
     * 
     * @param entityType the entity type
     * @return list of rules for the entity type
     */
    List<EnhancedValidationRule> findByEntityType(String entityType);
    
    /**
     * Find active rules by entity type.
     * 
     * @param entityType the entity type
     * @param active whether the rules are active
     * @return list of active rules for the entity type
     */
    List<EnhancedValidationRule> findByEntityTypeAndActive(String entityType, Boolean active);
    
    /**
     * Find rules by severity.
     * 
     * @param severity the severity
     * @return list of rules with the specified severity
     */
    List<EnhancedValidationRule> findBySeverity(ValidationSeverity severity);
    
    /**
     * Find active rules.
     * 
     * @param active whether the rules are active
     * @return list of active rules
     */
    List<EnhancedValidationRule> findByActive(Boolean active);
    
    /**
     * Find rules by category.
     * 
     * @param category the category
     * @return list of rules for the category
     */
    @Query("{ 'categories': ?0 }")
    List<EnhancedValidationRule> findByCategory(String category);
    
    /**
     * Find rules by primary attribute.
     * 
     * @param primaryAttribute the primary attribute
     * @return list of rules for the primary attribute
     */
    List<EnhancedValidationRule> findByPrimaryAttribute(String primaryAttribute);
    
    /**
     * Find rules that have a specific attribute in their attribute list.
     * 
     * @param attribute the attribute
     * @return list of rules that include the attribute
     */
    @Query("{ 'attributes': ?0 }")
    List<EnhancedValidationRule> findByAttributeInList(String attribute);
    
    /**
     * Find rules by condition type.
     * 
     * @param conditionType the condition type
     * @return list of rules with the specified condition type
     */
    List<EnhancedValidationRule> findByConditionType(String conditionType);
    
    /**
     * Find rules by entity type and category.
     * 
     * @param entityType the entity type
     * @param category the category
     * @return list of rules for the entity type and category
     */
    @Query("{ 'entityType': ?0, 'categories': ?1 }")
    List<EnhancedValidationRule> findByEntityTypeAndCategory(String entityType, String category);
    
    /**
     * Find rules by entity type and condition type.
     * 
     * @param entityType the entity type
     * @param conditionType the condition type
     * @return list of rules for the entity type and condition type
     */
    List<EnhancedValidationRule> findByEntityTypeAndConditionType(String entityType, String conditionType);
    
    /**
     * Find rules that depend on a specific rule.
     * 
     * @param ruleId the rule ID
     * @return list of rules that depend on the specified rule
     */
    @Query("{ 'dependsOn': ?0 }")
    List<EnhancedValidationRule> findByDependsOn(String ruleId);
    
    /**
     * Find rules by name.
     * 
     * @param name the rule name
     * @return list of rules with the specified name
     */
    List<EnhancedValidationRule> findByName(String name);
}