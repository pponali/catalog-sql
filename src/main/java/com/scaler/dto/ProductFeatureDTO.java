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
    private String type;
    private String unit;
    private Boolean required;
    private String validationPattern;
    private Double minValue;
    private Double maxValue;
    private List<String> allowedValues;
    private List<ProductFeatureValueDTO> values;
}
