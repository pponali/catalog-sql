package com.nosql.poc.rules.repository;

import com.nosql.poc.rules.model.SimpleValidationRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ValidationRuleRepository extends MongoRepository<SimpleValidationRule, String> {
    
    Optional<SimpleValidationRule> findByRuleId(String ruleId);
    
    List<SimpleValidationRule> findByEntityType(String entityType);
    
    List<SimpleValidationRule> findByCategory(String category);
    
    List<SimpleValidationRule> findByActiveTrue();
    
    List<SimpleValidationRule> findByAttribute(String attribute);
    
    List<SimpleValidationRule> findByEntityTypeAndAttribute(String entityType, String attribute);
    
    List<SimpleValidationRule> findByEntityTypeAndCategoryAndActiveTrue(String entityType, String category);
    
    @Query("{'entityType': ?0, 'attribute': ?1, 'condition': ?2}")
    List<SimpleValidationRule> findMatchingRules(String entityType, String attribute, String condition);
    
    @Query(value = "{'priority': {$gte: ?0}}", sort = "{'priority': -1}")
    List<SimpleValidationRule> findHighPriorityRules(int minPriority);
}