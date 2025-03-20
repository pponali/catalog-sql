package com.nosql.poc.rules.repository;

import com.nosql.poc.rules.model.SimpleBusinessRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusinessRuleRepository extends MongoRepository<SimpleBusinessRule, String> {
    
    Optional<SimpleBusinessRule> findByRuleId(String ruleId);
    
    List<SimpleBusinessRule> findByRuleType(String ruleType);
    
    List<SimpleBusinessRule> findByEntityType(String entityType);
    
    List<SimpleBusinessRule> findByCategory(String category);
    
    List<SimpleBusinessRule> findByActiveTrue();
    
    List<SimpleBusinessRule> findByStartDateBeforeAndEndDateAfterAndActiveTrue(LocalDateTime now, LocalDateTime now2);
    
    List<SimpleBusinessRule> findByRuleTypeAndActiveTrue(String ruleType);
    
    List<SimpleBusinessRule> findByEntityTypeAndRuleTypeAndActiveTrue(String entityType, String ruleType);
    
    List<SimpleBusinessRule> findByEntityTypeAndCategoryAndActiveTrue(String entityType, String category);
    
    @Query("{$and: [{'startDate': {$lte: ?0}}, {'endDate': {$gte: ?0}}, {'entityType': ?1}, {'active': true}]}")
    List<SimpleBusinessRule> findActiveRulesForEntityTypeAtTime(LocalDateTime dateTime, String entityType);
    
    @Query("{$and: [{'startDate': {$lte: ?0}}, {'endDate': {$gte: ?0}}, {'entityType': ?1}, {'ruleType': ?2}, {'active': true}]}")
    List<SimpleBusinessRule> findActiveRulesByTypeAndEntityAtTime(LocalDateTime dateTime, String entityType, String ruleType);
    
    @Query(value = "{'startDate': {$lte: ?0}, 'endDate': {$gte: ?0}, 'active': true}", sort = "{'priority': -1}")
    List<SimpleBusinessRule> findActiveRulesSortedByPriority(LocalDateTime now);
}