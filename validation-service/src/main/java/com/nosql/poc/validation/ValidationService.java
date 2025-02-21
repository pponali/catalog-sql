package com.nosql.poc.validation;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final KieContainer kieContainer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean validateFeature(ProductFeature feature, List<ValidationRules> rules) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            for (ValidationRules rule : rules) {
                kieSession.insert(rule);
            }
            kieSession.insert(feature);
            kieSession.fireAllRules();
            return feature.isValid();
        } finally {
            kieSession.dispose();
        }
    }

    public List<ValidationException> validateCategory(Category category) {
        List<ValidationException> exceptions = new ArrayList<>();
        if (StringUtils.isBlank(category.getName())) {
            exceptions.add(new ValidationException("Category name cannot be blank"));
        }
        return exceptions;
    }
}
