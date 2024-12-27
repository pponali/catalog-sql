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
    private String productType;
    private String status;
    private String metadata;
    private String sku;
    private List<Long> categoryIds = new ArrayList<>();
    private List<ProductFeatureDTO> features = new ArrayList<>();
}
