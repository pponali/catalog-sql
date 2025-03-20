package com.nosql.poc.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosql.poc.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Service for validating product features.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureValidationService {

    private final ValidationService validationService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Pattern> validationPatterns;
    
    public FeatureValidationService() {
        this.validationService = null; // This will be injected by Spring
        validationPatterns = new ConcurrentHashMap<>();
        validationPatterns.put("email", Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$"));
        validationPatterns.put("phone", Pattern.compile("^\\+?[1-9]\\d{1,14}$"));
        validationPatterns.put("url", Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$"));
    }
    
    /**
     * Adds a validation pattern.
     *
     * @param name the pattern name
     * @param pattern the regex pattern
     */
    public void addValidationPattern(String name, String pattern) {
        validationPatterns.put(name, Pattern.compile(pattern));
    }

    /**
     * Validates a product feature.
     *
     * @param featureData the feature data
     * @return validation result
     */
    public ValidationResult validateFeature(Map<String, Object> featureData) {
        log.debug("Validating product feature: {}", featureData.get("name"));
        
        // Perform basic validation
        ValidationResult result = new ValidationResult();
        result.setEntityType("PRODUCT_FEATURE");
        result.setValid(true);
        
        // Validate value type if present
        if (featureData.containsKey("valueType") && featureData.containsKey("values")) {
            String valueType = featureData.get("valueType").toString();
            List<String> values = (List<String>) featureData.get("values");
            
            // Check if values match the expected value type
            if (!validateValueType(valueType, values)) {
                result.setValid(false);
                result.getErrorMessages().add("Values do not match the expected value type: " + valueType);
            }
            
            // Check if values match any regex pattern
            if (featureData.containsKey("code")) {
                String code = featureData.get("code").toString();
                Pattern pattern = validationPatterns.get(code);
                if (pattern != null && values != null && !values.isEmpty()) {
                    boolean allMatch = true;
                    for (String value : values) {
                        if (!pattern.matcher(value).matches()) {
                            allMatch = false;
                            break;
                        }
                    }
                    if (!allMatch) {
                        result.setValid(false);
                        result.getErrorMessages().add("Values do not match the expected pattern for: " + code);
                    }
                }
            }
        }
        
        // If basic validation passed, perform rule-based validation
        if (result.isValid() && validationService != null) {
            JsonNode featureNode = objectMapper.valueToTree(featureData);
            result = validationService.validateEntity("PRODUCT_FEATURE", featureNode);
        }
        
        return result;
    }
    
    /**
     * Validates a feature value.
     *
     * @param featureValueData the feature value data
     * @return validation result
     */
    public ValidationResult validateFeatureValue(Map<String, Object> featureValueData) {
        log.debug("Validating feature value");
        
        ValidationResult result = new ValidationResult();
        result.setEntityType("FEATURE_VALUE");
        result.setValid(true);
        
        // Perform basic validation for feature values
        if (!featureValueData.containsKey("value")) {
            result.setValid(false);
            result.getErrorMessages().add("Feature value must contain a 'value' field");
        }
        
        // If basic validation passed, perform rule-based validation
        if (result.isValid() && validationService != null) {
            JsonNode featureValueNode = objectMapper.valueToTree(featureValueData);
            result = validationService.validateEntity("FEATURE_VALUE", featureValueNode);
        }
        
        return result;
    }
    
    /**
     * Validates a feature template.
     *
     * @param templateData the template data
     * @return validation result
     */
    public ValidationResult validateFeatureTemplate(Map<String, Object> templateData) {
        log.debug("Validating feature template: {}", templateData.get("name"));
        
        ValidationResult result = new ValidationResult();
        result.setEntityType("FEATURE_TEMPLATE");
        result.setValid(true);
        
        // Perform basic validation for feature templates
        if (!templateData.containsKey("name")) {
            result.setValid(false);
            result.getErrorMessages().add("Feature template must contain a 'name' field");
        }
        
        // If basic validation passed, perform rule-based validation
        if (result.isValid() && validationService != null) {
            JsonNode templateNode = objectMapper.valueToTree(templateData);
            result = validationService.validateEntity("FEATURE_TEMPLATE", templateNode);
        }
        
        return result;
    }
    
    /**
     * Validates that values match the expected value type.
     *
     * @param valueType the value type
     * @param values the values to validate
     * @return true if values match the expected type, false otherwise
     */
    private boolean validateValueType(String valueType, List<String> values) {
        if (values == null || values.isEmpty()) {
            return true;
        }
        
        switch (valueType) {
            case "STRING":
                return true; // All values are valid strings
                
            case "NUMBER":
                return values.stream().allMatch(this::isNumber);
                
            case "BOOLEAN":
                return values.stream().allMatch(v -> v.equalsIgnoreCase("true") || v.equalsIgnoreCase("false"));
                
            case "DATE":
                return values.stream().allMatch(this::isDate);
                
            default:
                return false;
        }
    }
    
    /**
     * Checks if a string represents a number.
     *
     * @param str the string to check
     * @return true if the string represents a number, false otherwise
     */
    private boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Checks if a string represents a date.
     *
     * @param str the string to check
     * @return true if the string represents a date, false otherwise
     */
    private boolean isDate(String str) {
        try {
            java.time.LocalDate.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}