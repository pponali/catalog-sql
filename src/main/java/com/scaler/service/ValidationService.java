package com.scaler.service;

import com.scaler.entity.ProductFeatureValue;
import com.scaler.validation.rule.ValidationRule;
import com.scaler.validation.fact.CategoryValidationFact;
import com.scaler.validation.fact.FeatureValidationFact;
import com.scaler.entity.Category;
import com.scaler.enums.FeatureValueType;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final KieContainer kieContainer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> validate(ProductFeatureValue featureValue, List<ValidationRule> rules) {
        KieSession kieSession = kieContainer.newKieSession();
        List<String> validationErrors = new ArrayList<>();

        try {
            kieSession.setGlobal("validationErrors", validationErrors);
            kieSession.insert(featureValue);
            rules.forEach(kieSession::insert);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }

        return validationErrors;
    }

    public List<String> validateCategoryFeature(String categoryCode, String featureCode, String value, Map<String, Object> metadata) {
        KieSession kieSession = kieContainer.newKieSession();
        List<String> validationErrors = new ArrayList<>();

        try {
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
        } finally {
            kieSession.dispose();
        }

        return validationErrors;
    }

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

    public List<String> validateBulkFeatures(List<ProductFeatureValue> features, Category category) {
        KieSession kieSession = kieContainer.newKieSession();
        List<String> validationErrors = new ArrayList<>();

        try {
            // Insert category context
            kieSession.insert(category);
            
            // Insert each feature for validation
            for (ProductFeatureValue feature : features) {
                String featureCode = null;
                if (feature.getFeature() != null && feature.getFeature().getTemplate() != null) {
                    featureCode = feature.getFeature().getTemplate().getCode();
                }
                
                Map<String, Object> metadata = null;
                if (feature.getAttributeValue() != null) {
                    metadata = objectMapper.convertValue(feature.getAttributeValue(), Map.class);
                }
                    
                CategoryValidationFact fact = CategoryValidationFact.builder()
                    .categoryCode(category.getCode())
                    .featureCode(featureCode)
                    .value(getFeatureValue(feature))
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
        } finally {
            kieSession.dispose();
        }

        return validationErrors;
    }

    private String getFeatureValue(ProductFeatureValue feature) {
        if (feature.getStringValue() != null) return feature.getStringValue();
        if (feature.getNumericValue() != null) return feature.getNumericValue().toString();
        if (feature.getBooleanValue() != null) return feature.getBooleanValue().toString();
        if (feature.getAttributeValue() != null) return feature.getAttributeValue().toString();
        return null;
    }
}
