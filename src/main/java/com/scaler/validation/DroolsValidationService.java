package com.scaler.validation;

import com.scaler.validation.fact.ValidationFact;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DroolsValidationService {

    private final KieContainer kieContainer;

    public DroolsValidationService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public void validateNumericValue(ValidationFact fact) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            if (fact.getNumericValue() == null) {
                fact.setValid(false);
                fact.setMessage("Numeric value is required");
                return;
            }
            kieSession.insert(fact);
            kieSession.fireAllRules();
            fact.setValid(fact.getNumericValue() != null);
        } finally {
            kieSession.dispose();
        }
    }

    public void validateStringValue(ValidationFact fact) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            if (fact.getStringValue() == null) {
                fact.setValid(false);
                fact.setMessage("String value is required");
                return;
            }
            
            if (fact.getPattern() != null) {
                if (!fact.getStringValue().matches(fact.getPattern())) {
                    fact.setValid(false);
                    fact.setMessage("Value does not match pattern: " + fact.getPattern());
                    return;
                }
            }
            
            kieSession.insert(fact);
            kieSession.fireAllRules();
            fact.setValid(fact.getStringValue() != null);
        } finally {
            kieSession.dispose();
        }
    }

    public void validateAllowedValues(ValidationFact fact) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            if (fact.getStringValue() == null) {
                fact.setValid(false);
                fact.setMessage("Value is required");
                return;
            }

            String[] allowedValues = fact.getAllowedValues().toArray(new String[0]);
            if (!Arrays.asList(allowedValues).contains(fact.getStringValue())) {
                fact.setValid(false);
                fact.setMessage("Value is not in allowed values: " + String.join(", ", allowedValues));
                return;
            }

            kieSession.insert(fact);
            kieSession.fireAllRules();
            fact.setValid(true);
        } finally {
            kieSession.dispose();
        }
    }

    public void validateRange(ValidationFact fact) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            if (fact.getNumericValue() == null) {
                fact.setValid(false);
                fact.setMessage("Numeric value is required");
                return;
            }

            Double value = fact.getNumericValue();
            Double minValue = fact.getMinValue();
            Double maxValue = fact.getMaxValue();

            if (minValue != null && value < minValue) {
                fact.setValid(false);
                fact.setMessage("Value is less than minimum allowed: " + minValue);
                return;
            }

            if (maxValue != null && value > maxValue) {
                fact.setValid(false);
                fact.setMessage("Value is greater than maximum allowed: " + maxValue);
                return;
            }

            kieSession.insert(fact);
            kieSession.fireAllRules();
            fact.setValid(true);
        } finally {
            kieSession.dispose();
        }
    }
}
