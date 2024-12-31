package com.scaler.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.enums.FeatureValueType;
import com.scaler.exception.ValidationException;
import com.scaler.validation.fact.CategoryValidationFact;
import com.scaler.validation.fact.FeatureValidationFact;
import com.scaler.validation.rule.ValidationRules;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsible for validating product features and categories using Drools rules engine.
 * Implements validation logic for both individual and bulk feature validations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final KieContainer kieContainer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Validates a single product feature value against provided validation rules.
     * @param featureValue The feature value to validate
     * @param rules List of validation rules to apply
     * @return List of validation error messages
     * @throws ValidationException if validation setup fails
     */
    public List<String> validate(ProductFeatureValue featureValue, List<ValidationRules> rules) {
        if (featureValue == null) {
            throw new ValidationException("Feature value cannot be null");
        }
        if (rules == null || rules.isEmpty()) {
            log.warn("No validation rules provided for feature validation");
            return new ArrayList<>();
        }

        KieSession kieSession = null;
        List<String> validationErrors = new ArrayList<>();

        try {
            kieSession = kieContainer.newKieSession();
            String value = getFeatureValue(featureValue);
            
            if (value == null) {
                validationErrors.add("Feature value cannot be null");
                return validationErrors;
            }

            log.debug("Validating feature value: {} against {} rules", value, rules.size());
            kieSession.setGlobal("validationErrors", validationErrors);
            kieSession.insert(featureValue);
            rules.forEach(kieSession::insert);
            kieSession.fireAllRules();
            
            if (!validationErrors.isEmpty()) {
                log.warn("Validation failed with {} errors", validationErrors.size());
            }
        } catch (Exception e) {
            log.error("Error during feature validation", e);
            throw new ValidationException("Failed to validate feature: " + e.getMessage(), e);
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }

        return validationErrors;
    }

    /**
     * Validates category-specific feature constraints.
     * @param categoryCode Category identifier
     * @param featureCode Feature identifier
     * @param value Feature value to validate
     * @param metadata Additional validation metadata
     * @return List of validation error messages
     * @throws ValidationException if validation setup fails
     */
    public List<String> validateCategoryFeature(String categoryCode, String featureCode, String value, Map<String, Object> metadata) {
        if (StringUtils.isBlank(categoryCode) || StringUtils.isBlank(featureCode)) {
            throw new ValidationException("Category code and feature code must not be empty");
        }

        KieSession kieSession = null;
        List<String> validationErrors = new ArrayList<>();

        try {
            kieSession = kieContainer.newKieSession();
            log.debug("Validating category feature - Category: {}, Feature: {}", categoryCode, featureCode);
            
            CategoryValidationFact fact = CategoryValidationFact.builder()
                .categoryCode(categoryCode)
                .featureCode(featureCode)
                .value(value)
                .metadata(metadata)
                .errors(new ArrayList<>())
                .build();

            kieSession.insert(fact);
            kieSession.fireAllRules();
            validationErrors.addAll(fact.getErrors());
            
            if (!validationErrors.isEmpty()) {
                log.warn("Category feature validation failed with {} errors", validationErrors.size());
            }
        } catch (Exception e) {
            log.error("Error during category feature validation", e);
            throw new ValidationException("Failed to validate category feature: " + e.getMessage(), e);
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }

        return validationErrors;
    }

    /**
     * Validates a single feature value against its type and constraints.
     * @param featureCode Feature identifier
     * @param value Feature value to validate
     * @param valueTypeStr Feature value type (e.g., STRING, NUMBER, BOOLEAN)
     * @return List of validation error messages
     * @throws ValidationException if validation setup fails
     */
    public List<String> validateFeature(String featureCode, String value, String valueTypeStr) {
        KieSession kieSession = kieContainer.newKieSession();
        List<String> validationErrors = new ArrayList<>();

        try {
            FeatureValueType valueType = FeatureValueType.valueOf(valueTypeStr);
            FeatureValidationFact fact = FeatureValidationFact.builder()
                .featureCode(featureCode)
                .value(value)
                .valueType(valueType)
                .isList(valueType.isList())
                .valid(true)
                .validationErrors(new ArrayList<>())
                .build();

            kieSession.insert(fact);
            kieSession.fireAllRules();
            validationErrors.addAll(fact.getValidationErrors());
        } finally {
            kieSession.dispose();
        }

        return validationErrors;
    }

    /**
     * Validates bulk features for a given category.
     * @param features List of feature values to validate
     * @param category Category context for validation
     * @return List of validation error messages
     * @throws ValidationException if validation setup fails
     */
    public List<String> validateBulkFeatures(List<ProductFeatureValue> features, Category category) {
        if (features == null || features.isEmpty()) {
            throw new ValidationException("Features list cannot be null or empty");
        }
        if (category == null) {
            throw new ValidationException("Category cannot be null");
        }

        KieSession kieSession = null;
        List<String> validationErrors = new ArrayList<>();

        try {
            kieSession = kieContainer.newKieSession();
            log.debug("Starting bulk validation for {} features in category {}", features.size(), category.getCode());
            
            // Insert category context
            kieSession.insert(category);
            
            // Insert each feature for validation
            for (ProductFeatureValue feature : features) {
                String featureCode = Optional.ofNullable(feature)
                    .map(ProductFeatureValue::getFeature)
                    .map(ProductFeature::getTemplate)
                    .map(CategoryFeatureTemplate::getCode)
                    .orElse(null);

                if (featureCode == null) {
                    log.warn("Feature code is null for feature in bulk validation");
                    continue;
                }

                Map<String, Object> metadata = feature.getAttributeValues() != null ?
                    objectMapper.convertValue(feature.getAttributeValues(), Map.class) : null;

                String value = getFeatureValue(feature);
                if (value == null) {
                    validationErrors.add(String.format("Feature value cannot be null for feature code: %s", featureCode));
                    continue;
                }

                CategoryValidationFact fact = CategoryValidationFact.builder()
                    .categoryCode(category.getCode())
                    .featureCode(featureCode)
                    .value(value)
                    .metadata(metadata)
                    .errors(new ArrayList<>())
                    .build();
                
                kieSession.insert(fact);
            }
            
            kieSession.fireAllRules();
            
            // Collect all validation errors
            kieSession.getObjects(obj -> obj instanceof CategoryValidationFact)
                .forEach(obj -> {
                    CategoryValidationFact fact = (CategoryValidationFact) obj;
                    if (fact.getErrors() != null) {
                        validationErrors.addAll(fact.getErrors());
                    }
                });

            if (!validationErrors.isEmpty()) {
                log.warn("Bulk validation failed with {} errors", validationErrors.size());
            }
        } catch (Exception e) {
            log.error("Error during bulk feature validation", e);
            throw new ValidationException("Failed to validate bulk features: " + e.getMessage(), e);
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }

        return validationErrors;
    }

    private String getFeatureValue(ProductFeatureValue feature) {
        if (feature == null || feature.getAttributeValues() == null) {
            return null;
        }
        
        try {
            JsonNode valueNode = feature.getAttributeValues().get("value");
            return valueNode != null ? valueNode.asText() : null;
        } catch (Exception e) {
            log.error("Error getting feature value", e);
            return null;
        }
    }
}
