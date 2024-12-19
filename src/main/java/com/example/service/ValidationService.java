package com.example.service;

import com.example.entity.ProductFeatureValue;
import com.example.validation.rule.ValidationRule;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final KieContainer kieContainer;

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
}
