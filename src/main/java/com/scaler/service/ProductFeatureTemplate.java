package com.scaler.service;

import com.scaler.entity.CategoryFeatureTemplate;
import jakarta.validation.constraints.NotNull;

public class ProductFeatureTemplate {
    public static String getCode(@NotNull CategoryFeatureTemplate categoryFeatureTemplate) {
        return categoryFeatureTemplate.getCode();
    }
}
