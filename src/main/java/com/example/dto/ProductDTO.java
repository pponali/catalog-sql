package com.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    
    @NotNull
    @NotEmpty
    private String code;
    
    @NotNull
    @NotEmpty
    private String name;
    
    private String description;
    private boolean active;
    
    @NotNull
    @NotEmpty
    private Set<Long> categoryIds = new HashSet<>();
    
    private Set<ProductFeatureDTO> features = new HashSet<>();
}
