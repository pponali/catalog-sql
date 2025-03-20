package com.nosql.poc.validation.repository;

import com.nosql.poc.validation.model.SimpleValidationRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ValidationRuleRepository extends MongoRepository<SimpleValidationRule, String> {
    
    Optional<SimpleValidationRule> findByRuleId(String ruleId);
    
    List<SimpleValidationRule> findByEntityType(String entityType);
    
    List<SimpleValidationRule> findByAttribute(String attribute);
    
    List<SimpleValidationRule> findByAttributeAndEntityType(String attribute, String entityType);
    
    List<SimpleValidationRule> findByActive(Boolean active);
    
    List<SimpleValidationRule> findBySeverity(String severity);
    
    List<SimpleValidationRule> findByEntityTypeAndActive(String entityType, Boolean active);
    
    @Query("{'active': true}")
    List<SimpleValidationRule> findActiveRules();
    
    @Query("{'entityType': ?0, 'active': true}")
    List<SimpleValidationRule> findActiveRulesByEntityType(String entityType);
    
    @Query("{'attribute': ?0, 'entityType': ?1, 'active': true}")
    List<SimpleValidationRule> findActiveRulesByAttributeAndEntityType(String attribute, String entityType);
    
    @Query("{'category': ?0, 'active': true}")
    List<SimpleValidationRule> findActiveRulesByCategory(String category);
    
    @Query("{'category': ?0, 'entityType': ?1, 'active': true}")
    List<SimpleValidationRule> findActiveRulesByCategoryAndEntityType(String category, String entityType);
}