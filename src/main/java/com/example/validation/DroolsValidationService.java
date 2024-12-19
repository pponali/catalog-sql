package com.example.validation;

import com.example.entity.ProductFeature;
import com.example.entity.ProductFeatureValue;
import com.example.enums.FeatureValueType;
import com.example.validation.fact.ValidationFact;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DroolsValidationService {
    
    private final KieSession kieSession;

    public List<String> validateFeature(ProductFeature feature) {
        List<String> validationErrors = new ArrayList<>();

        // Insert feature into session
        kieSession.insert(feature);
        
        // Add validation facts
        ValidationFact fact = ValidationFact.builder()
            .featureCode(feature.getTemplate().getCode())
            .valueType(feature.getTemplate().getAttributeType())
            .values(feature.getValues().stream()
                .map(ProductFeatureValue::getValue)
                .toList())
            .multiValued(feature.getTemplate().isMultiValued())
            .errors(validationErrors)
            .build();
        
        kieSession.insert(fact);
        
        // Fire rules
        kieSession.fireAllRules();
        
        return validationErrors;
    }

    public List<String> validateValues(ProductFeature feature, List<String> values) {
        return values.stream()
            .flatMap(value -> validateValue(feature, value).stream())
            .distinct()
            .collect(Collectors.toList());
    }

    public boolean isValid(ProductFeature feature, List<String> values) {
        if (!feature.isListType() && values.size() > 1) {
            return false;
        }
        return validateValues(feature, values).isEmpty();
    }
}
