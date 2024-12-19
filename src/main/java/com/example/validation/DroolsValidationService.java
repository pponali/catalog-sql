package com.example.validation;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ProductFeatureValue;
import com.example.validation.fact.ValidationFact;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DroolsValidationService {
    private final KieContainer kieContainer;

    public boolean validate(ProductFeatureValue value, CategoryFeatureTemplate template) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            ValidationFact fact = ValidationFact.builder()
                .featureCode(template.getCode())
                .value(value.getValue())
                .attributeType(template.getAttributeType())
                .validationPattern(template.getValidationPattern())
                .minValue(template.getMinValue())
                .maxValue(template.getMaxValue())
                .allowedValues(template.getAllowedValues())
                .unit(template.getUnit())
                .isValid(true)
                .build();

            kieSession.insert(fact);
            kieSession.fireAllRules();

            return fact.isValid();
        } finally {
            kieSession.dispose();
        }
    }
}
