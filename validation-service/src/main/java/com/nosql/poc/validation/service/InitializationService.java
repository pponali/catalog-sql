package com.nosql.poc.validation.service;

import com.nosql.poc.validation.model.EnhancedValidationRule;
import com.nosql.poc.validation.model.RuleConditionType;
import com.nosql.poc.validation.model.SimpleValidationRule;
import com.nosql.poc.validation.model.ValidationSeverity;
import com.nosql.poc.validation.repository.EnhancedValidationRuleRepository;
import com.nosql.poc.validation.repository.ValidationRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitializationService {

    private final ValidationRuleRepository validationRuleRepository;
    private final EnhancedValidationRuleRepository enhancedRuleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        log.info("Initializing validation rules...");
        
        // Only initialize if no rules exist
        if (validationRuleRepository.count() == 0) {
            createSimpleValidationRules();
        }
        
        if (enhancedRuleRepository.count() == 0) {
            createEnhancedValidationRules();
        }
        
        log.info("Validation rules initialized successfully.");
    }
    
    private void createSimpleValidationRules() {
        log.info("Creating simple validation rules...");
        
        // Product name validation
        SimpleValidationRule productNameRule = new SimpleValidationRule();
        productNameRule.setRuleId("PROD_NAME_REQUIRED");
        productNameRule.setName("Product Name Required");
        productNameRule.setDescription("Product name is required");
        productNameRule.setEntityType("PRODUCT");
        productNameRule.setAttribute("name");
        productNameRule.setCondition("NOT_NULL");
        productNameRule.setMessage("Product name is required");
        productNameRule.setSeverity("ERROR");
        productNameRule.setPriority(10);
        productNameRule.setActive(true);
        productNameRule.setCategory("BASIC");
        validationRuleRepository.save(productNameRule);
        
        // Product price validation
        SimpleValidationRule productPriceRule = new SimpleValidationRule();
        productPriceRule.setRuleId("PROD_PRICE_POSITIVE");
        productPriceRule.setName("Product Price Positive");
        productPriceRule.setDescription("Product price must be positive");
        productPriceRule.setEntityType("PRODUCT");
        productPriceRule.setAttribute("price");
        productPriceRule.setCondition("MIN_VALUE");
        productPriceRule.setConditionValue("0");
        productPriceRule.setMessage("Product price must be positive");
        productPriceRule.setSeverity("ERROR");
        productPriceRule.setPriority(8);
        productPriceRule.setActive(true);
        productPriceRule.setCategory("BASIC");
        validationRuleRepository.save(productPriceRule);
        
        // Category name validation
        SimpleValidationRule categoryNameRule = new SimpleValidationRule();
        categoryNameRule.setRuleId("CAT_NAME_REQUIRED");
        categoryNameRule.setName("Category Name Required");
        categoryNameRule.setDescription("Category name is required");
        categoryNameRule.setEntityType("CATEGORY");
        categoryNameRule.setAttribute("name");
        categoryNameRule.setCondition("NOT_NULL");
        categoryNameRule.setMessage("Category name is required");
        categoryNameRule.setSeverity("ERROR");
        categoryNameRule.setPriority(10);
        categoryNameRule.setActive(true);
        categoryNameRule.setCategory("BASIC");
        validationRuleRepository.save(categoryNameRule);
        
        log.info("Created {} simple validation rules", 3);
    }
    
    private void createEnhancedValidationRules() {
        log.info("Creating enhanced validation rules...");
        
        // Rule 1: Product name validation - between 3 and 100 characters
        EnhancedValidationRule nameRule = new EnhancedValidationRule();
        nameRule.setRuleId("PROD_NAME_LENGTH");
        nameRule.setName("Product Name Length");
        nameRule.setDescription("Product name must be between 3 and 100 characters");
        nameRule.setEntityType("PRODUCT");
        nameRule.setPrimaryAttribute("name");
        nameRule.setConditionType(RuleConditionType.CONDITIONAL);
        nameRule.setConditionExpression("name != null");
        nameRule.addConditionalExpression("then", "name.length() >= 3 && name.length() <= 100");
        nameRule.setMessage("Product name must be between 3 and 100 characters");
        nameRule.setSeverity(ValidationSeverity.ERROR);
        nameRule.setPriority(10);
        nameRule.setActive(true);
        nameRule.setCreatedAt(LocalDateTime.now());
        nameRule.setUpdatedAt(LocalDateTime.now());
        enhancedRuleRepository.save(nameRule);
        
        // Rule 2: Product price validation - must be positive
        EnhancedValidationRule priceRule = new EnhancedValidationRule();
        priceRule.setRuleId("PROD_PRICE_POSITIVE");
        priceRule.setName("Product Price Positive");
        priceRule.setDescription("Product price must be positive");
        priceRule.setEntityType("PRODUCT");
        priceRule.setPrimaryAttribute("price");
        priceRule.setConditionType(RuleConditionType.CONDITIONAL);
        priceRule.setConditionExpression("price != null");
        priceRule.addConditionalExpression("then", "price > 0");
        priceRule.setMessage("Product price must be positive");
        priceRule.setSeverity(ValidationSeverity.ERROR);
        priceRule.setPriority(10);
        priceRule.setActive(true);
        priceRule.setCreatedAt(LocalDateTime.now());
        priceRule.setUpdatedAt(LocalDateTime.now());
        enhancedRuleRepository.save(priceRule);
        
        // Rule 3: Cross-field validation - discount price must be less than regular price
        EnhancedValidationRule discountRule = new EnhancedValidationRule();
        discountRule.setRuleId("PROD_DISCOUNT_PRICE");
        discountRule.setName("Product Discount Price");
        discountRule.setDescription("Discount price must be less than regular price");
        discountRule.setEntityType("PRODUCT");
        discountRule.addAttribute("price");
        discountRule.addAttribute("discountPrice");
        discountRule.setPrimaryAttribute("discountPrice");
        discountRule.setConditionType(RuleConditionType.CROSS_FIELD);
        discountRule.setConditionExpression("discountPrice == null || price == null || discountPrice < price");
        discountRule.setMessage("Discount price must be less than regular price");
        discountRule.setSeverity(ValidationSeverity.ERROR);
        discountRule.setPriority(5);
        discountRule.setActive(true);
        discountRule.setCreatedAt(LocalDateTime.now());
        discountRule.setUpdatedAt(LocalDateTime.now());
        enhancedRuleRepository.save(discountRule);
        
        // Rule 4: Calculated field - discount percentage
        EnhancedValidationRule discountPercentRule = new EnhancedValidationRule();
        discountPercentRule.setRuleId("PROD_DISCOUNT_PERCENT");
        discountPercentRule.setName("Product Discount Percentage");
        discountPercentRule.setDescription("Discount percentage must be reasonable");
        discountPercentRule.setEntityType("PRODUCT");
        discountPercentRule.addAttribute("price");
        discountPercentRule.addAttribute("discountPrice");
        discountPercentRule.setPrimaryAttribute("discountPrice");
        discountPercentRule.setConditionType(RuleConditionType.CALCULATED);
        discountPercentRule.setCalculationExpression("price != null && discountPrice != null ? (price - discountPrice) / price * 100 : 0");
        discountPercentRule.setConditionExpression("calculatedValue <= 90");
        discountPercentRule.setMessage("Discount percentage cannot exceed 90%");
        discountPercentRule.setSeverity(ValidationSeverity.WARNING);
        discountPercentRule.setPriority(3);
        discountPercentRule.setActive(true);
        discountPercentRule.setCreatedAt(LocalDateTime.now());
        discountPercentRule.setUpdatedAt(LocalDateTime.now());
        enhancedRuleRepository.save(discountPercentRule);
        
        // Rule 5: Category name validation
        EnhancedValidationRule categoryNameRule = new EnhancedValidationRule();
        categoryNameRule.setRuleId("CAT_NAME_LENGTH");
        categoryNameRule.setName("Category Name Length");
        categoryNameRule.setDescription("Category name must be between 2 and 50 characters");
        categoryNameRule.setEntityType("CATEGORY");
        categoryNameRule.setPrimaryAttribute("name");
        categoryNameRule.setConditionType(RuleConditionType.CONDITIONAL);
        categoryNameRule.setConditionExpression("name != null");
        categoryNameRule.addConditionalExpression("then", "name.length() >= 2 && name.length() <= 50");
        categoryNameRule.setMessage("Category name must be between 2 and 50 characters");
        categoryNameRule.setSeverity(ValidationSeverity.ERROR);
        categoryNameRule.setPriority(10);
        categoryNameRule.setActive(true);
        categoryNameRule.setCreatedAt(LocalDateTime.now());
        categoryNameRule.setUpdatedAt(LocalDateTime.now());
        enhancedRuleRepository.save(categoryNameRule);
        
        log.info("Created {} enhanced validation rules", 5);
    }
}