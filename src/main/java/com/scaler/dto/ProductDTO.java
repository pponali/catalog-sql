package com.scaler.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String code;
    private List<Long> categoryIds = new ArrayList<>();
    private List<ProductFeatureDTO> features = new ArrayList<>();
}
