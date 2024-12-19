package com.example.validation;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ProductFeatureValue;

public interface ValidationService {
    /**
     * Validate a value against the rules defined in a category feature template
     * @param value The value to validate
     * @param template The category feature template containing validation rules
     * @return true if the value is valid, false otherwise
     */
    boolean validateValue(ProductFeatureValue value, CategoryFeatureTemplate template);
}
