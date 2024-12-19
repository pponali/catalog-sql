package com.example.dto;

import lombok.Data;

@Data
public class ProductFeatureValueDTO {
    private Long id;
    private Long featureId;
    private String value;
    private String unit;
}
