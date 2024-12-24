package com.scaler.repository;

import com.scaler.model.ValidationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValidationRuleRepository extends JpaRepository<ValidationRule, Long> {
    
    List<ValidationRule> findByFeatureIdAndActiveTrue(Long featureId);
    
    List<ValidationRule> findByFeatureIdAndRuleTypeAndActiveTrue(Long featureId, ValidationRule.RuleType ruleType);
    
    List<ValidationRule> findByFeatureIdOrderByPriorityAsc(Long featureId);
    
    boolean existsByFeatureIdAndRuleExpressionAndActiveTrue(Long featureId, String ruleExpression);
    
    Optional<ValidationRule> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}
