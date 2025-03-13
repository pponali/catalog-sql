package com.scaler.repository;

import com.scaler.model.ValidationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ValidationRule entities
 */
@Repository
public interface ValidationRuleRepository extends JpaRepository<ValidationRule, UUID> {

    /**
     * Find validation rules by category ID
     *
     * @param categoryId The category ID
     * @return List of validation rules
     */
    List<ValidationRule> findByCategoryId(UUID categoryId);

    /**
     * Find validation rules by category ID and feature code
     *
     * @param categoryId The category ID
     * @param featureCode The feature code
     * @return List of validation rules
     */
    List<ValidationRule> findByCategoryIdAndFeatureCode(UUID categoryId, String featureCode);

    /**
     * Find validation rules by feature ID
     * 
     * @param featureId The feature ID
     * @return List of validation rules
     */
    List<ValidationRule> findByFeatureId(UUID featureId);
    
    /**
     * Find validation rules by rule type
     * 
     * @param ruleType The rule type
     * @return List of validation rules
     */
    List<ValidationRule> findByRuleType(String ruleType);

    /**
     * Find active validation rules by category ID
     *
     * @param categoryId The category ID
     * @param active Whether the rule is active
     * @return List of validation rules
     */
    List<ValidationRule> findByCategoryIdAndActive(UUID categoryId, boolean active);

    /**
     * Find a validation rule by code
     * 
     * @param code The code of the validation rule
     * @return The validation rule
     */
    Optional<ValidationRule> findByCode(String code);

    /**
     * Delete a validation rule by code
     * 
     * @param code The code of the validation rule
     */
    void deleteByCode(String code);

    /**
     * Check if a validation rule exists by code
     * 
     * @param code The code of the validation rule
     * @return Whether the validation rule exists
     */
    boolean existsByCode(String code);
}
