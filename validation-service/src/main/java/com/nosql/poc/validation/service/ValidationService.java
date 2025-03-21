package com.scaler.service;

import com.scaler.entity.ProductFeatureValue;
import com.scaler.model.ValidationRule;
import com.scaler.validation.rule.ValidationRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for validation operations using Drools rules engine
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);

    /**
     * Validates a product feature value using Drools rules engine
     *
     * @param productFeatureValue The product feature value to validate
     * @param rules               The validation rules to apply
     * @return List of validation error messages, empty if validation passes
     */
    public List<String> validateProductFeatureValue(ProductFeatureValue productFeatureValue, List<ValidationRule> rules) {
        // Create a KieSession to execute the rules
        KieSession kieSession = createKieSession();

        // Create a list to hold validation errors
        List<String> validationErrors = new ArrayList<>();

        try {
            // Set the global variable for validation errors
            kieSession.setGlobal("validationErrors", validationErrors);

            // Insert the validation rules into the session
            for (ValidationRule rule : rules) {
                kieSession.insert(rule);
            }

            // Insert the product feature value to validate
            kieSession.insert(productFeatureValue);

            // Fire all rules
            kieSession.fireAllRules();

            // Log the validation results
            if (validationErrors.isEmpty()) {
                logger.info("Validation passed for product feature value: {}", productFeatureValue.getId());
            } else {
                logger.warn("Validation failed for product feature value: {}. Errors: {}",
                        productFeatureValue.getId(), validationErrors);
            }

            return validationErrors;
        } finally {
            // Always dispose the session to release resources
            kieSession.dispose();
        }
    }

    /**
     * Creates a KieSession for executing Drools rules
     *
     * @return A new KieSession
     */
    private KieSession createKieSession() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Add the validation.drl file to the KieFileSystem
        kieFileSystem.write(ResourceFactory.newClassPathResource("rules/validation.drl"));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        KieRepository kieRepository = kieServices.getRepository();
        KieModule kieModule = kieRepository.getKieModule(kieBuilder.getKieModule().getReleaseId());

        return kieServices.newKieContainer(kieModule.getReleaseId()).newKieSession();
    }

    /**
     * Converts ValidationRules entity to ValidationRule model
     *
     * @param validationRules The ValidationRules entity from the database
     * @return A ValidationRule model that can be used with the rules engine
     */
    public ValidationRule convertToValidationRule(ValidationRules validationRules) {
        ValidationRule.RuleType ruleType = ValidationRule.RuleType.valueOf(validationRules.getRuleType());

        ValidationRule rule = ValidationRule.builder()
                .code(validationRules.getCode())
                .name(validationRules.getName())
                .description(validationRules.getDescription())
                .ruleType(ruleType.toString())
                .active(true)
                .priority(0)
                .build();

        // Set the rule expression based on the rule type
        switch (ruleType) {
            case RuleType.PATTERN:
                rule.setRuleExpression(validationRules.getPattern());
                break;
            case RuleType.RANGE:
                rule.setRuleExpression(validationRules.getMinValue() + "," + validationRules.getMaxValue());
                break;
            case RuleType.ALLOWED_VALUES:
                rule.setRuleExpression(validationRules.getAllowedValues());
                break;
            default:
                rule.setRuleExpression("");
                break;
        }

        return rule;
    }
}
