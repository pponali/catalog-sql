package com.example.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class ProductFeatureDTO {
    private Long id;
    
    @NotNull
    private Long productId;
    
    @NotNull
    @NotEmpty
    private String templateCode;
    
    private String templateName;
    private String attributeType;
    
    @Valid
    private Set<ProductFeatureValueDTO> values;
}
