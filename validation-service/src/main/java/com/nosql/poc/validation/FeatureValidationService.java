package com.nosql.poc.validation;

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
        return false;
    }
}
