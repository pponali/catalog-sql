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
        ProductDTO dto = productMapper.toDTO(product);
        if (product.getFeatures() != null) {
            dto.setFeatures(new ArrayList<>(product.getFeatures().stream()
                .map(this::mapFeatureWithValues)
                .collect(Collectors.toList())));
        }
        return dto;
    }

    private ProductFeatureDTO mapFeatureWithValues(ProductFeature feature) {
        ProductFeatureDTO featureDTO = productFeatureMapper.toDTO(feature);
        Set<ProductFeatureValueDTO> featureValues = new HashSet<>();
        
        // Create feature value based on the feature type and default value
        ProductFeatureValueDTO value = ProductFeatureValueDTO.builder()
            .id(UUID.randomUUID())
            .productId(feature.getProduct().getId())
            .featureTemplateId(feature.getTemplate().getId())
            .featureId(feature.getId())
            .type(feature.getFeatureType())
            .unit(feature.getUnitOfMeasure() != null ? feature.getUnitOfMeasure().getCode() : null)
            .status("ACTIVE")
            .createdBy("system")
            .lastModifiedBy("system")
            .createdDate(LocalDateTime.now().toString())
            .lastModifiedDate(LocalDateTime.now().toString())
            .build();

        if (feature.getDefaultValue() != null) {
            JsonNode attributeValue;
            switch (feature.getFeatureType().toUpperCase()) {
                case "NUMERIC":
                    try {
                        attributeValue = JsonNodeFactory.instance.numberNode(
                            Double.parseDouble(feature.getDefaultValue()));
                    } catch (NumberFormatException e) {
                        attributeValue = JsonNodeFactory.instance.nullNode();
                    }
                    break;
                case "BOOLEAN":
                    attributeValue = JsonNodeFactory.instance.booleanNode(
                        Boolean.parseBoolean(feature.getDefaultValue()));
                    break;
                case "JSON":
                    try {
                        attributeValue = objectMapper.readTree(feature.getDefaultValue());
                    } catch (Exception e) {
                        attributeValue = JsonNodeFactory.instance.nullNode();
                    }
                    break;
                case "STRING":
                case "ENUM":
                default:
                    attributeValue = JsonNodeFactory.instance.textNode(feature.getDefaultValue());
                    break;
            }
            value.setAttributeValues(attributeValue.toString());
        }

        featureValues.add(value);
        featureDTO.setFeatureValues(featureValues);
        featureDTO.setDefaultValue(null); // Remove defaultValue as it's now in featureValues
        return featureDTO;
    }

    public Product toEntity(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        if (dto.getFeatures() != null) {
            Set<ProductFeature> features = dto.getFeatures().stream()
                .map(productFeatureMapper::toEntity)
                .collect(Collectors.toSet());
            product.setFeatures(features);
            features.forEach(feature -> feature.setProduct(product));
        }
        return product;
    }
}
