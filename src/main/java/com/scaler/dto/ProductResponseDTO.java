package com.scaler.dto;

import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProductResponseDTO {
    private String code;
    private String name;
    private String description;
    private String productType;
    private String status;
    private String metadata;
    private String sku;
    private Double price;
    private Set<CategoryResponseDTO> categories;

    public static ProductResponseDTO fromEntity(Product product) {
        if (product == null) return null;
        
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setProductType(product.getProductType().toString());
        dto.setStatus(product.getStatus());
        dto.setMetadata(product.getMetadata());
        dto.setSku(product.getSku());
        dto.setPrice(product.getPrice());
        
        // Convert categories without creating circular reference
        if (product.getProductCategories() != null) {
            dto.setCategories(product.getProductCategories().stream()
                .map(ProductCategory::getCategory)
                .map(CategoryResponseDTO::fromEntity)
                .collect(Collectors.toSet()));
        }
        
        return dto;
    }
}
