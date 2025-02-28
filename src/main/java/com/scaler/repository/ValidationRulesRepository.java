package com.scaler.repository;

import com.scaler.validation.rule.ValidationRules;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidationRulesRepository extends JpaRepository<ValidationRules, Long> {
}