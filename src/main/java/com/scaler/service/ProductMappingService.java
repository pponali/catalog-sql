package com.scaler.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.scaler.dto.ProductDTO;
import com.scaler.dto.ProductFeatureDTO;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.mapper.ProductFeatureMapper;
import com.scaler.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductMappingService {

    private final ProductMapper productMapper;
    private final ProductFeatureMapper productFeatureMapper;
    private final ObjectMapper objectMapper;

    public ProductDTO toDTO(Product product) {
        return productMapper.toDTO(product);
    }



    public Product toEntity(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        
        // Initialize collections
        if (product.getProductCategories() == null) {
            product.setProductCategories(new HashSet<>());
        }
        if (product.getFeatureMappings() == null) {
            product.setFeatureMappings(new HashSet<>());
        }
        if (product.getAttributes() == null) {
            product.setAttributes(new ArrayList<>());
        }
        if (product.getProductChannels() == null) {
            product.setProductChannels(new HashSet<>());
        }
        
        return product;
    }
}
