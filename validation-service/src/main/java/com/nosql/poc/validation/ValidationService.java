package com.nosql.poc.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosql.poc.validation.core.EnhancedValidationEngine;
import com.nosql.poc.validation.core.ValidationEngine;
import com.nosql.poc.validation.model.EnhancedValidationRule;
import com.nosql.poc.validation.model.SimpleValidationRule;
import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;
import com.nosql.poc.validation.model.ValidationSeverity;
import com.nosql.poc.validation.repository.EnhancedValidationRuleRepository;
import com.nosql.poc.validation.repository.ValidationRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main validation service that utilizes the validation engine to apply rules to entities.
 * This service supports both simple and enhanced validation rules.
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class ValidationService {

    private final ValidationEngine validationEngine;
    private final EnhancedValidationEngine enhancedValidationEngine;
    private final ValidationRuleRepository validationRuleRepository;
    private final EnhancedValidationRuleRepository enhancedRuleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Validates an entity against all applicable rules.
     *
     * @param entityType the type of entity (PRODUCT, CATEGORY, etc.)
     * @param entity the entity to validate as a JSON object
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateEntity(String entityType, JsonNode entity) {
        log.debug("Validating {} entity", entityType);
        
        List<SimpleValidationRule> rules = validationRuleRepository.findByEntityTypeAndActive(entityType, true);
        
        ValidationContext context = new ValidationContext();
        context.setEntityType(entityType);
        context.setEntity(entity);
        context.setRules(rules);
        context.setValidationType("BASIC");
        
        return validationEngine.validate(context);
    }
    
    /**
     * Validates an entity using the enhanced validation engine with cross-field validation,
     * conditional rules, and rule dependencies.
     *
     * @param entityType the type of entity (PRODUCT, CATEGORY, etc.)
     * @param entity the entity to validate as a JSON object
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateEntityEnhanced(String entityType, JsonNode entity) {
        log.debug("Enhanced validation for {} entity", entityType);
        
        List<EnhancedValidationRule> rules = enhancedRuleRepository.findByEntityTypeAndActive(entityType, true);
        
        ValidationContext context = ValidationContext.forEnhancedRules(entity.path("id").asText());
        context.setEntityType(entityType);
        context.setEntity(entity);
        context.setEnhancedRules(rules);
        
        // Add metadata to track validation request
        context.addParameter("validationTimestamp", LocalDateTime.now().toString());
        context.addParameter("validationMethod", "enhanced");
        
        return enhancedValidationEngine.validate(context);
    }
    
    /**
     * Validates a product.
     *
     * @param productData the product data as a Map
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateProduct(Map<String, Object> productData) {
        log.debug("Validating product");
        
        JsonNode productNode = objectMapper.valueToTree(productData);
        return validateEntity("PRODUCT", productNode);
    }
    
    /**
     * Validates a product with enhanced validation.
     *
     * @param productData the product data as a Map
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateProductEnhanced(Map<String, Object> productData) {
        log.debug("Enhanced validation for product");
        
        JsonNode productNode = objectMapper.valueToTree(productData);
        return validateEntityEnhanced("PRODUCT", productNode);
    }
    
    /**
     * Validates a category.
     *
     * @param categoryData the category data as a Map
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateCategory(Map<String, Object> categoryData) {
        log.debug("Validating category");
        
        // Basic validation for required fields
        ValidationResult result = new ValidationResult();
        result.setEntityType("CATEGORY");
        result.setValid(true);
        
        if (categoryData.get("name") == null || 
            StringUtils.isBlank(categoryData.get("name").toString())) {
            result.setValid(false);
            result.getErrorMessages().add("Category name cannot be blank");
            result.addError("CATEGORY_NAME_REQUIRED", "Category name cannot be blank");
        }
        
        // Continue with rule-based validation if basic validation passed
        if (result.isValid()) {
            JsonNode categoryNode = objectMapper.valueToTree(categoryData);
            result = validateEntity("CATEGORY", categoryNode);
        }
        
        return result;
    }
    
    /**
     * Validates a category with enhanced validation.
     *
     * @param categoryData the category data as a Map
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateCategoryEnhanced(Map<String, Object> categoryData) {
        log.debug("Enhanced validation for category");
        
        // Basic validation for required fields
        ValidationResult result = new ValidationResult();
        result.setEntityType("CATEGORY");
        result.setValid(true);
        
        if (categoryData.get("name") == null || 
            StringUtils.isBlank(categoryData.get("name").toString())) {
            result.setValid(false);
            result.getErrorMessages().add("Category name cannot be blank");
            result.addError("CATEGORY_NAME_REQUIRED", "Category name cannot be blank");
        }
        
        // Continue with enhanced rule-based validation if basic validation passed
        if (result.isValid()) {
            JsonNode categoryNode = objectMapper.valueToTree(categoryData);
            result = validateEntityEnhanced("CATEGORY", categoryNode);
        }
        
        return result;
    }
    
    /**
     * Validate product feature.
     *
     * @param featureData the feature data
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateProductFeature(Map<String, Object> featureData) {
        log.debug("Validating product feature");
        
        JsonNode featureNode = objectMapper.valueToTree(featureData);
        return validateEntity("PRODUCT_FEATURE", featureNode);
    }
    
    /**
     * Validate product feature with enhanced validation.
     *
     * @param featureData the feature data
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateProductFeatureEnhanced(Map<String, Object> featureData) {
        log.debug("Enhanced validation for product feature");
        
        JsonNode featureNode = objectMapper.valueToTree(featureData);
        return validateEntityEnhanced("PRODUCT_FEATURE", featureNode);
    }
    
    /**
     * Validates a seller.
     *
     * @param sellerData the seller data as a Map
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateSeller(Map<String, Object> sellerData) {
        log.debug("Validating seller");
        
        JsonNode sellerNode = objectMapper.valueToTree(sellerData);
        return validateEntity("SELLER", sellerNode);
    }
    
    /**
     * Validates a seller with enhanced validation.
     *
     * @param sellerData the seller data as a Map
     * @return ValidationResult with details about validation status
     */
    public ValidationResult validateSellerEnhanced(Map<String, Object> sellerData) {
        log.debug("Enhanced validation for seller");
        
        JsonNode sellerNode = objectMapper.valueToTree(sellerData);
        return validateEntityEnhanced("SELLER", sellerNode);
    }
    
    /**
     * Get all simple validation rules.
     *
     * @return list of all simple validation rules
     */
    public List<SimpleValidationRule> getAllRules() {
        return validationRuleRepository.findAll();
    }
    
    /**
     * Get all enhanced validation rules.
     *
     * @return list of all enhanced validation rules
     */
    public List<EnhancedValidationRule> getAllEnhancedRules() {
        return enhancedRuleRepository.findAll();
    }
    
    /**
     * Get simple validation rules by entity type.
     *
     * @param entityType the entity type
     * @return list of simple validation rules for the entity type
     */
    public List<SimpleValidationRule> getRulesByEntityType(String entityType) {
        return validationRuleRepository.findByEntityType(entityType);
    }
    
    /**
     * Get enhanced validation rules by entity type.
     *
     * @param entityType the entity type
     * @return list of enhanced validation rules for the entity type
     */
    public List<EnhancedValidationRule> getEnhancedRulesByEntityType(String entityType) {
        return enhancedRuleRepository.findByEntityType(entityType);
    }
    
    /**
     * Get active simple validation rules.
     *
     * @return list of active simple validation rules
     */
    public List<SimpleValidationRule> getActiveRules() {
        return validationRuleRepository.findByActive(true);
    }
    
    /**
     * Get active enhanced validation rules.
     *
     * @return list of active enhanced validation rules
     */
    public List<EnhancedValidationRule> getActiveEnhancedRules() {
        return enhancedRuleRepository.findByActive(true);
    }
    
    /**
     * Get simple validation rules by severity.
     *
     * @param severity the severity level
     * @return list of simple validation rules with the specified severity
     */
    public List<SimpleValidationRule> getRulesBySeverity(String severity) {
        return validationRuleRepository.findBySeverity(severity);
    }
    
    /**
     * Get enhanced validation rules by severity.
     *
     * @param severity the severity level
     * @return list of enhanced validation rules with the specified severity
     */
    public List<EnhancedValidationRule> getEnhancedRulesBySeverity(String severityString) {
        ValidationSeverity severity = ValidationSeverity.valueOf(severityString);
        return enhancedRuleRepository.findBySeverity(severity);
    }
    
    /**
     * Create or update a simple validation rule.
     *
     * @param rule the simple validation rule to save
     * @return the saved rule
     */
    public SimpleValidationRule saveRule(SimpleValidationRule rule) {
        return validationRuleRepository.save(rule);
    }
    
    /**
     * Create or update an enhanced validation rule.
     *
     * @param rule the enhanced validation rule to save
     * @return the saved rule
     */
    public EnhancedValidationRule saveEnhancedRule(EnhancedValidationRule rule) {
        return enhancedRuleRepository.save(rule);
    }
    
    /**
     * Delete a simple validation rule.
     *
     * @param id the rule ID
     */
    public void deleteRule(String id) {
        validationRuleRepository.deleteById(id);
    }
    
    /**
     * Delete an enhanced validation rule.
     *
     * @param id the rule ID
     */
    public void deleteEnhancedRule(String id) {
        enhancedRuleRepository.deleteById(id);
    }
    
    /**
     * Create sample enhanced validation rules for products.
     *
     * @return List of created rules
     */
    public List<EnhancedValidationRule> createSampleProductRules() {
        List<EnhancedValidationRule> rules = new ArrayList<>();
        
        // Rule 1: Product name validation - between 3 and 100 characters
        EnhancedValidationRule nameRule = new EnhancedValidationRule();
        nameRule.setRuleId("PROD_NAME_LENGTH");
        nameRule.setName("Product Name Length");
        nameRule.setDescription("Product name must be between 3 and 100 characters");
        nameRule.setEntityType("PRODUCT");
        nameRule.setPrimaryAttribute("name");
        nameRule.setConditionType(com.nosql.poc.validation.model.RuleConditionType.CONDITIONAL);
        nameRule.setConditionExpression("name != null");
        nameRule.addConditionalExpression("then", "name.length() >= 3 && name.length() <= 100");
        nameRule.setMessage("Product name must be between 3 and 100 characters");
        nameRule.setSeverity(com.nosql.poc.validation.model.ValidationSeverity.ERROR);
        nameRule.setPriority(10);
        nameRule.setActive(true);
        nameRule.setCreatedAt(LocalDateTime.now());
        nameRule.setUpdatedAt(LocalDateTime.now());
        rules.add(nameRule);
        
        // Rule 2: Product price validation - must be positive
        EnhancedValidationRule priceRule = new EnhancedValidationRule();
        priceRule.setRuleId("PROD_PRICE_POSITIVE");
        priceRule.setName("Product Price Positive");
        priceRule.setDescription("Product price must be positive");
        priceRule.setEntityType("PRODUCT");
        priceRule.setPrimaryAttribute("price");
        priceRule.setConditionType(com.nosql.poc.validation.model.RuleConditionType.CONDITIONAL);
        priceRule.setConditionExpression("price != null");
        priceRule.addConditionalExpression("then", "price > 0");
        priceRule.setMessage("Product price must be positive");
        priceRule.setSeverity(com.nosql.poc.validation.model.ValidationSeverity.ERROR);
        priceRule.setPriority(10);
        priceRule.setActive(true);
        priceRule.setCreatedAt(LocalDateTime.now());
        priceRule.setUpdatedAt(LocalDateTime.now());
        rules.add(priceRule);
        
        // Rule 3: Cross-field validation - discount price must be less than regular price
        EnhancedValidationRule discountRule = new EnhancedValidationRule();
        discountRule.setRuleId("PROD_DISCOUNT_PRICE");
        discountRule.setName("Product Discount Price");
        discountRule.setDescription("Discount price must be less than regular price");
        discountRule.setEntityType("PRODUCT");
        discountRule.addAttribute("price");
        discountRule.addAttribute("discountPrice");
        discountRule.setPrimaryAttribute("discountPrice");
        discountRule.setConditionType(com.nosql.poc.validation.model.RuleConditionType.CROSS_FIELD);
        discountRule.setConditionExpression("discountPrice == null || price == null || discountPrice < price");
        discountRule.setMessage("Discount price must be less than regular price");
        discountRule.setSeverity(com.nosql.poc.validation.model.ValidationSeverity.ERROR);
        discountRule.setPriority(5);
        discountRule.setActive(true);
        discountRule.setCreatedAt(LocalDateTime.now());
        discountRule.setUpdatedAt(LocalDateTime.now());
        rules.add(discountRule);
        
        // Rule 4: Calculated field - discount percentage
        EnhancedValidationRule discountPercentRule = new EnhancedValidationRule();
        discountPercentRule.setRuleId("PROD_DISCOUNT_PERCENT");
        discountPercentRule.setName("Product Discount Percentage");
        discountPercentRule.setDescription("Discount percentage must be reasonable");
        discountPercentRule.setEntityType("PRODUCT");
        discountPercentRule.addAttribute("price");
        discountPercentRule.addAttribute("discountPrice");
        discountPercentRule.setPrimaryAttribute("discountPrice");
        discountPercentRule.setConditionType(com.nosql.poc.validation.model.RuleConditionType.CALCULATED);
        discountPercentRule.setCalculationExpression("price != null && discountPrice != null ? (price - discountPrice) / price * 100 : 0");
        discountPercentRule.setConditionExpression("calculatedValue <= 90");
        discountPercentRule.setMessage("Discount percentage cannot exceed 90%");
        discountPercentRule.setSeverity(com.nosql.poc.validation.model.ValidationSeverity.WARNING);
        discountPercentRule.setPriority(3);
        discountPercentRule.setActive(true);
        discountPercentRule.setCreatedAt(LocalDateTime.now());
        discountPercentRule.setUpdatedAt(LocalDateTime.now());
        rules.add(discountPercentRule);
        
        // Rule 5: Conditional rule - SKU format
        EnhancedValidationRule skuRule = new EnhancedValidationRule();
        skuRule.setRuleId("PROD_SKU_FORMAT");
        skuRule.setName("Product SKU Format");
        skuRule.setDescription("Product SKU must follow the correct format");
        skuRule.setEntityType("PRODUCT");
        skuRule.setPrimaryAttribute("sku");
        skuRule.setConditionType(com.nosql.poc.validation.model.RuleConditionType.REGEX);
        skuRule.setConditionValue("^[A-Z]{3}-\\d{4}$");
        skuRule.setMessage("SKU must be in format XXX-0000 (3 uppercase letters followed by hyphen and 4 digits)");
        skuRule.setSeverity(com.nosql.poc.validation.model.ValidationSeverity.ERROR);
        skuRule.setPriority(8);
        skuRule.setActive(true);
        skuRule.setCreatedAt(LocalDateTime.now());
        skuRule.setUpdatedAt(LocalDateTime.now());
        rules.add(skuRule);
        
        // Save all rules to the repository
        List<EnhancedValidationRule> savedRules = new ArrayList<>();
        for (EnhancedValidationRule rule : rules) {
            savedRules.add(enhancedRuleRepository.save(rule));
        }
        
        return savedRules;
    }
    
    /**
     * Create sample enhanced validation rules for categories.
     *
     * @return List of created rules
     */
    public List<EnhancedValidationRule> createSampleCategoryRules() {
        List<EnhancedValidationRule> rules = new ArrayList<>();
        
        // Rule 1: Category name validation
        EnhancedValidationRule nameRule = new EnhancedValidationRule();
        nameRule.setRuleId("CAT_NAME_LENGTH");
        nameRule.setName("Category Name Length");
        nameRule.setDescription("Category name must be between 2 and 50 characters");
        nameRule.setEntityType("CATEGORY");
        nameRule.setPrimaryAttribute("name");
        nameRule.setConditionType(com.nosql.poc.validation.model.RuleConditionType.CONDITIONAL);
        nameRule.setConditionExpression("name != null");
        nameRule.addConditionalExpression("then", "name.length() >= 2 && name.length() <= 50");
        nameRule.setMessage("Category name must be between 2 and 50 characters");
        nameRule.setSeverity(com.nosql.poc.validation.model.ValidationSeverity.ERROR);
        nameRule.setPriority(10);
        nameRule.setActive(true);
        rules.add(nameRule);
        
        // Rule 2: Category code format
        EnhancedValidationRule codeRule = new EnhancedValidationRule();
        codeRule.setRuleId("CAT_CODE_FORMAT");
        codeRule.setName("Category Code Format");
        codeRule.setDescription("Category code must be uppercase and alphanumeric");
        codeRule.setEntityType("CATEGORY");
        codeRule.setPrimaryAttribute("code");
        codeRule.setConditionType(com.nosql.poc.validation.model.RuleConditionType.REGEX);
        codeRule.setConditionValue("^[A-Z0-9_]+$");
        codeRule.setMessage("Category code must contain only uppercase letters, numbers, and underscores");
        codeRule.setSeverity(com.nosql.poc.validation.model.ValidationSeverity.ERROR);
        codeRule.setPriority(8);
        codeRule.setActive(true);
        rules.add(codeRule);
        
        // Save all rules to the repository
        List<EnhancedValidationRule> savedRules = new ArrayList<>();
        for (EnhancedValidationRule rule : rules) {
            savedRules.add(enhancedRuleRepository.save(rule));
        }
        
        return savedRules;
    }
}