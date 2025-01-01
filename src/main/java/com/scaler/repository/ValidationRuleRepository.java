package com.scaler.repository;

import com.scaler.model.ValidationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ValidationRuleRepository extends JpaRepository<ValidationRule, UUID> {
    
    List<ValidationRule> findByFeatureIdAndActiveTrue(UUID featureId);
    
    List<ValidationRule> findByFeatureIdAndRuleTypeAndActiveTrue(UUID featureId, ValidationRule.RuleType ruleType);
    
    List<ValidationRule> findByFeatureIdOrderByPriorityAsc(UUID featureId);
    
    boolean existsByFeatureIdAndRuleExpressionAndActiveTrue(UUID featureId, String ruleExpression);
    
    Optional<ValidationRule> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}
