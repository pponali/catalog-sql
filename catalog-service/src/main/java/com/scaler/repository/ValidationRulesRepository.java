package com.scaler.repository;

import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.validation.rule.ValidationRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for ValidationRules entities
 */
@Repository
public interface ValidationRulesRepository extends JpaRepository<ValidationRules, Long> {
    
    /**
     * Find validation rules by template
     * 
     * @param template The category feature template
     * @return List of validation rules
     */
    List<ValidationRules> findByTemplate(CategoryFeatureTemplate template);
    
    /**
     * Find validation rules by code
     * 
     * @param code The rule code
     * @return List of validation rules
     */
    List<ValidationRules> findByCode(String code);
    
    /**
     * Find validation rules by rule type
     * 
     * @param ruleType The rule type
     * @return List of validation rules
     */
    List<ValidationRules> findByRuleType(String ruleType);
    
    /**
     * Find validation rules by template ID
     * 
     * @param templateId The template ID
     * @return List of validation rules
     */
    List<ValidationRules> findByTemplateId(UUID templateId);
}
