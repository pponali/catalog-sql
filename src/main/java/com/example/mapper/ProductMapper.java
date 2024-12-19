package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ProductFeatureMapper featureMapper;

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setSku(product.getSku());
        dto.setFeatures(product.getFeatures().stream()
            .map(featureMapper::toDTO)
            .toList());
        return dto;
    }

    public void updateEntityFromDTO(ProductDTO dto, Product product) {
        if (dto == null) {
            return;
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setSku(dto.getSku());
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        updateEntityFromDTO(dto, product);
        return product;
    }
}
