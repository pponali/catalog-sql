package com.scaler.validation.service.impl;

import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.ValidationRuleRepository;
import com.scaler.repository.ValidationRulesRepository;
import com.scaler.validation.rule.ValidationRules;
import com.scaler.validation.service.CategoryValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of CategoryValidationService for category-specific product validation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryValidationServiceImpl implements CategoryValidationService {

    private final ValidationRuleRepository validationRuleRepository;
    private final ValidationRulesRepository validationRulesRepository;
    private final KieContainer kieContainer;

    @Override
    public Map<String, List<String>> validateProduct(Product product, Category category) {
        Map<String, List<String>> validationResults = new HashMap<>();
        
        // Get all product features
        Set<ProductFeatureMapping> featureMappings = product.getFeatureMappings();
        if (featureMappings == null || featureMappings.isEmpty()) {
            return validationResults;
        }
        
        // Validate each feature
        for (ProductFeatureMapping mapping : featureMappings) {
            ProductFeature feature = mapping.getFeature();
            List<String> errors = validateProductFeature(feature, category);
            
            if (!errors.isEmpty()) {
                validationResults.put(feature.getCode(), errors);
            }
            
            // Validate feature values
            Set<ProductFeatureValueMapping> valueMappings = product.getFeatureValueMappings();
            if (valueMappings != null && !valueMappings.isEmpty()) {
                for (ProductFeatureValueMapping valueMapping : valueMappings) {
                    ProductFeatureValue value = valueMapping.getFeatureValue();
                    List<String> valueErrors = validateProductFeatureValue(value, feature, category);
                    
                    if (!valueErrors.isEmpty()) {
                        String key = feature.getCode() + "_value";
                        validationResults.computeIfAbsent(key, k -> new ArrayList<>()).addAll(valueErrors);
                    }
                }
            }
        }
        
        return validationResults;
    }

    @Override
    public List<String> validateProductFeature(ProductFeature feature, Category category) {
        List<String> errors = new ArrayList<>();
        
        // Get validation rules for the feature
        List<ValidationRule> rules = getValidationRulesForCategoryAndFeature(category, feature.getCode());
        
        // Apply each rule
        for (ValidationRule rule : rules) {
            if (!rule.isActive()) {
                continue;
            }
            
            switch (ValidationRule.RuleType.valueOf(rule.getRuleType())) {
                case REQUIRED:
                    if (feature.getValue() == null || feature.getValue().isEmpty()) {
                        errors.add(rule.getName() + ": Value is required");
                    }
                    break;
                case PATTERN:
                    if (feature.getValue() != null && !feature.getValue().matches(rule.getPattern())) {
                        errors.add(rule.getName() + ": Value does not match pattern");
                    }
                    break;
                case RANGE:
                    if (feature.getValue() != null) {
                        try {
                            double value = Double.parseDouble(feature.getValue());
                            Double min = rule.getMinValue();
                            Double max = rule.getMaxValue();
                            
                            if ((min != null && value < min) || (max != null && value > max)) {
                                errors.add(rule.getName() + ": Value must be between " + min + " and " + max);
                            }
                        } catch (NumberFormatException e) {
                            errors.add(rule.getName() + ": Value must be a number");
                        }
                    }
                    break;
                case ALLOWED_VALUES:
                    if (feature.getValue() != null && !rule.getAllowedValues().contains(feature.getValue())) {
                        errors.add(rule.getName() + ": Value must be one of " + String.join(", ", rule.getAllowedValues()));
                    }
                    break;
                case CUSTOM:
                    // Use Drools for custom validation
                    KieSession kieSession = kieContainer.newKieSession();
                    try {
                        List<String> customErrors = new ArrayList<>();
                        kieSession.setGlobal("errors", customErrors);
                        kieSession.insert(feature);
                        kieSession.insert(category);
                        kieSession.fireAllRules();
                        
                        errors.addAll(customErrors);
                    } finally {
                        kieSession.dispose();
                    }
                    break;
                default:
                    break;
            }
        }
        
        return errors;
    }

    @Override
    public List<String> validateProductFeatureValue(ProductFeatureValue featureValue, ProductFeature feature, Category category) {
        List<String> errors = new ArrayList<>();
        
        // Get validation rules for the feature
        List<ValidationRule> rules = getValidationRulesForCategoryAndFeature(category, feature.getCode());
        
        // Apply each rule
        for (ValidationRule rule : rules) {
            if (!rule.isActive()) {
                continue;
            }
            
            String value = featureValue.getValueAsString();
            
            switch (ValidationRule.RuleType.valueOf(rule.getRuleType())) {
                case REQUIRED:
                    if (value == null || value.isEmpty()) {
                        errors.add(rule.getName() + ": Value is required");
                    }
                    break;
                case PATTERN:
                    if (value != null && !value.matches(rule.getPattern())) {
                        errors.add(rule.getName() + ": Value does not match pattern");
                    }
                    break;
                case RANGE:
                    if (value != null) {
                        try {
                            double numValue = Double.parseDouble(value);
                            Double min = rule.getMinValue();
                            Double max = rule.getMaxValue();
                            
                            if ((min != null && numValue < min) || (max != null && numValue > max)) {
                                errors.add(rule.getName() + ": Value must be between " + min + " and " + max);
                            }
                        } catch (NumberFormatException e) {
                            errors.add(rule.getName() + ": Value must be a number");
                        }
                    }
                    break;
                case ALLOWED_VALUES:
                    if (value != null && !rule.getAllowedValues().contains(value)) {
                        errors.add(rule.getName() + ": Value must be one of " + String.join(", ", rule.getAllowedValues()));
                    }
                    break;
                case CUSTOM:
                    // Use Drools for custom validation
                    KieSession kieSession = kieContainer.newKieSession();
                    try {
                        List<String> customErrors = new ArrayList<>();
                        kieSession.setGlobal("errors", customErrors);
                        kieSession.insert(featureValue);
                        kieSession.insert(feature);
                        kieSession.insert(category);
                        kieSession.fireAllRules();
                        
                        errors.addAll(customErrors);
                    } finally {
                        kieSession.dispose();
                    }
                    break;
                default:
                    break;
            }
        }
        
        return errors;
    }

    @Override
    public List<ValidationRule> getValidationRulesForCategory(Category category) {
        return validationRuleRepository.findByCategoryId(category.getId());
    }

    @Override
    public List<ValidationRule> getValidationRulesForCategoryAndFeature(Category category, String featureCode) {
        return validationRuleRepository.findByCategoryIdAndFeatureCode(category.getId(), featureCode);
    }

    @Override
    public List<ValidationRules> createValidationRulesForCategory(Category category) {
        List<ValidationRules> createdRules = new ArrayList<>();
        
        // Get all feature templates for the category
        Set<CategoryFeatureTemplate> templates = category.getTemplates();
        if (templates == null || templates.isEmpty()) {
            return createdRules;
        }
        
        // Create validation rules for each template
        for (CategoryFeatureTemplate template : templates) {
            // Create required rule if the feature is mandatory
            if (template.isMandatory()) {
                ValidationRules requiredRule = ValidationRules.createRequiredRule(template);
                requiredRule = validationRulesRepository.save(requiredRule);
                createdRules.add(requiredRule);
            }
            
            // Create type rule
            ValidationRules typeRule = ValidationRules.createTypeRule(template);
            typeRule = validationRulesRepository.save(typeRule);
            createdRules.add(typeRule);
            
            // Create range rule if min and max values are specified
            if (template.getMinValue() != null && template.getMaxValue() != null) {
                ValidationRules rangeRule = ValidationRules.createRangeRule(template);
                rangeRule = validationRulesRepository.save(rangeRule);
                createdRules.add(rangeRule);
            }
            
            // Create pattern rule if validation pattern is specified
            if (template.getValidationPattern() != null && !template.getValidationPattern().isEmpty()) {
                ValidationRules patternRule = ValidationRules.createPatternRule(template);
                patternRule = validationRulesRepository.save(patternRule);
                createdRules.add(patternRule);
            }
            
            // Create allowed values rule if allowed values are specified
            if (template.getAllowedValues() != null && !template.getAllowedValues().isEmpty()) {
                ValidationRules allowedValuesRule = ValidationRules.createAllowedValuesRule(template);
                allowedValuesRule = validationRulesRepository.save(allowedValuesRule);
                createdRules.add(allowedValuesRule);
            }
        }
        
        return createdRules;
    }
}
