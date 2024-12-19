package com.example.validation.rule;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ProductFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationRuleService {

    public List<ValidationRule> getValidationRules(ProductFeature feature) {
        List<ValidationRule> rules = new ArrayList<>();
        CategoryFeatureTemplate template = feature.getTemplate();

        // Required validation
        if (template.isMandatory()) {
            rules.add(ValidationRule.createRequiredRule(template));
        }

        // Type validation
        rules.add(ValidationRule.createTypeRule(template));

        // Range validation for numeric values
        if ("numeric".equalsIgnoreCase(template.getAttributeType()) &&
            (template.getMinValue() != null || template.getMaxValue() != null)) {
            rules.add(ValidationRule.createRangeRule(template));
        }

        // Pattern validation for strings
        if ("string".equalsIgnoreCase(template.getAttributeType()) &&
            template.getValidationPattern() != null) {
            rules.add(ValidationRule.createRegexRule(template));
        }

        return rules;
    }

    public boolean validateFeature(ProductFeature feature) {
        List<ValidationRule> rules = getValidationRules(feature);
        
        // If no values and feature is not required, it's valid
        if (feature.getValues().isEmpty() && !feature.getTemplate().isMandatory()) {
            return true;
        }

        // Check multi-value constraint
        if (!feature.getTemplate().isMultiValued() && feature.getValues().size() > 1) {
            return false;
        }

        // Validate each value against all rules
        return feature.getValues().stream()
            .allMatch(value -> rules.stream()
                .allMatch(rule -> rule.validate(value.getValue())));
    }
}
