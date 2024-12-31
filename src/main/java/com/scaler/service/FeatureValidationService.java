package com.scaler.service;

import com.scaler.dto.ProductFeatureValidationDTO;
import com.scaler.enums.FeatureValueType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class FeatureValidationService {
    private final Map<String, Pattern> validationPatterns;
    
    public FeatureValidationService() {
        validationPatterns = new ConcurrentHashMap<>();
        // Add default patterns
        validationPatterns.put("email", Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$"));
        validationPatterns.put("phone", Pattern.compile("^\\+?[1-9]\\d{1,14}$"));
        validationPatterns.put("url", Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$"));
    }

    public void addValidationPattern(String name, String pattern) {
        validationPatterns.put(name, Pattern.compile(pattern));
    }

    public boolean validateFeature(ProductFeatureValidationDTO feature) {
        if (!feature.isValidValueType()) {
            return false;
        }

        List<String> values = feature.getValues();
        if (values == null || values.isEmpty()) {
            return true;
        }

        Pattern pattern = validationPatterns.get(feature.getCode());
        if (pattern != null) {
            return values.stream().allMatch(value -> pattern.matcher(value).matches());
        }

        // Default validation based on valueType
        return switch (feature.getValueType()) {
            case STRING, STRING_LIST -> true;
            case NUMERIC, NUMERIC_LIST -> values.stream()
                .allMatch(value -> value.matches("^-?\\d*\\.?\\d+$"));
            case BOOLEAN, BOOLEAN_LIST -> values.stream()
                .allMatch(value -> value.matches("^(true|false)$"));
            default -> false;
        };
    }

    public void validateAndNormalizeValues(ProductFeatureValidationDTO feature) {
        if (feature.getValues() == null || feature.getValues().isEmpty()) {
            return;
        }

        List<String> normalizedValues = feature.getValues().stream()
            .map(value -> normalizeValue(value, feature.getValueType()))
            .toList();
        
        feature.setValues(normalizedValues);
    }

    private String normalizeValue(String value, FeatureValueType valueType) {
        if (value == null) {
            return null;
        }

        return switch (valueType) {
            case NUMERIC, NUMERIC_LIST -> value.trim().replaceAll("[^\\d.-]", "");
            case BOOLEAN, BOOLEAN_LIST -> Boolean.valueOf(value.trim()).toString();
            default -> value.trim();
        };
    }
}
