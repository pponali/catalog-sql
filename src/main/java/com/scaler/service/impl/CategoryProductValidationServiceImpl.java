package com.scaler.service.impl;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeatureMapping;
import com.scaler.repository.ValidationRuleRepository;
import com.scaler.service.CategoryProductValidationService;
import com.scaler.validation.fact.FeatureValidationFact;
import com.scaler.validation.service.CategoryValidationService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CategoryProductValidationServiceImpl implements CategoryProductValidationService {

    private final KieContainer kieContainer;
    private final CategoryValidationService categoryValidationService;
    private final ValidationRuleRepository validationRuleRepository;

    @Autowired
    public CategoryProductValidationServiceImpl(KieContainer kieContainer, CategoryValidationService categoryValidationService, ValidationRuleRepository validationRuleRepository) {
        this.kieContainer = kieContainer;
        this.categoryValidationService = categoryValidationService;
        this.validationRuleRepository = validationRuleRepository;
    }

    @Override
    public Map<String, List<String>> validateProduct(Product product) {
        Map<String, List<String>> validationResults = new HashMap<>();

        for (ProductFeatureMapping featureMapping : product.getFeatureMappings()) {
            String featureCode = featureMapping.getFeature().getCode();
            String featureValue = null;//featureMapping.getValue();
            List<String> errors = validateProductFeature(product, featureCode, featureValue);
            if (!errors.isEmpty()) {
                validationResults.put(featureCode, errors);
            }
        }

        return validationResults;
    }

    @Override
    public List<String> validateProductFeature(Product product, String featureCode, String featureValue) {
        KieSession kieSession = kieContainer.newKieSession();
        List<String> errors = new ArrayList<>();

        FeatureValidationFact fact = createFeatureValidationFact(product, featureCode, featureValue);
        kieSession.insert(fact);
        kieSession.setGlobal("errors", errors);

        kieSession.fireAllRules();
        kieSession.dispose();

        return errors;
    }

    @Override
    public FeatureValidationFact createFeatureValidationFact(Product product, String featureCode, String featureValue) {
        FeatureValidationFact fact = new FeatureValidationFact();
        fact.setProductId(product.getId());
        fact.setFeatureCode(featureCode);
        //fact.setFeatureValue(featureValue);

        Category category = product.getCategory();
        /*if (category != null) {
            fact.setCategoryId(category.getId());
        }*/

        return fact;
    }
}
