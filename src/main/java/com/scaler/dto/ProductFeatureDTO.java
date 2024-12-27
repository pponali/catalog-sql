package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureDTO {
    private Long id;
    private String name;
    private String description;
    private String code;
    private String featureType;
    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private Long unitId;
    private String metadata;
    private boolean required;
    private Long productId;
    private Long templateId;
    private List<String> values;
}
