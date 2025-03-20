package com.scaler.rules.repository;

import com.scaler.rules.model.BusinessRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRuleRepository extends MongoRepository<BusinessRule, String> {
    
    List<BusinessRule> findByDomainAndRuleTypeAndActive(String domain, String ruleType, Boolean active);
    
    @Query("{'categories': ?0, 'active': true}")
    List<BusinessRule> findByCategoryIdAndActive(String categoryId);
    
    List<BusinessRule> findByActiveOrderByPriorityDesc(Boolean active);
}