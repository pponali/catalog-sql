package com.scaler.mapper;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.ProductFeatureDTO;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.dto.ProductFeatureWithValuesDTO;
import com.scaler.entity.Product;

import com.scaler.entity.ProductFeatureValueMapping;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProductMapperDecorator implements ProductMapper {

    @Autowired
    @Qualifier("delegate")
    private ProductMapper delegate;

    @Autowired
    private ProductFeatureMapper featureMapper;

    @Autowired
    private ProductFeatureValueMapper featureValueMapper;

    @Override
    public ProductDTO toDTO(Product entity) {
        ProductDTO dto = delegate.toDTO(entity);
        if (entity == null || dto == null) {
            return dto;
        }

        // Map features with their values
        if (entity.getFeatureValueMappings() != null) {
            // Group mappings by feature
            var featureGroups = entity.getFeatureValueMappings().stream()
                .collect(Collectors.groupingBy(ProductFeatureValueMapping::getFeature));

            Set<ProductFeatureWithValuesDTO> featuresWithValues = featureGroups.entrySet().stream()
                .map(entry -> {
                    var feature = entry.getKey();
                    if (feature == null) return null;

                    // Map feature values from the mappings
                    Set<ProductFeatureValueDTO> values = entry.getValue().stream()
                        .map(mapping -> featureValueMapper.toDTO(mapping.getFeatureValue()))
                        .filter(v -> v != null)
                        .collect(Collectors.toSet());

                    // Create feature with values using the decorator
                    return ((ProductFeatureMapperDecorator) featureMapper).toDTOWithValues(feature, values);
                })
                .filter(f -> f != null)
                .collect(Collectors.toSet());
            dto.setFeatures(featuresWithValues);
        } else {
            dto.setFeatures(new HashSet<>());
        }

        return dto;
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        Product entity = delegate.toEntity(dto);
        if (dto == null || entity == null) {
            return entity;
        }

        // Initialize feature value mappings set
        entity.setFeatureValueMappings(new HashSet<>());

        // Create feature value mappings from features
        if (dto.getFeatures() != null) {
            dto.getFeatures().forEach(featureWithValuesDTO -> {
                // Convert DTO back to regular ProductFeatureDTO for mapping
                ProductFeatureDTO featureDTO = new ProductFeatureDTO();
                BeanUtils.copyProperties(featureWithValuesDTO, featureDTO);
                
                var feature = featureMapper.toEntity(featureDTO);
                if (feature != null) {
                    // Add feature values
                    if (featureWithValuesDTO.getValues() != null) {
                        featureWithValuesDTO.getValues().forEach(valueDTO -> {
                            var value = featureValueMapper.toEntity(valueDTO);
                            if (value != null) {
                                var valueMapping = ProductFeatureValueMapping.builder()
                                    .product(entity)
                                    .feature(feature)
                                    .featureValue(value)
                                    .displayOrder(1)
                                    .visible(true)
                                    .enabled(true)
                                    .isPrimary(false)
                                    .isActive(true)
                                    .createdBy(dto.getCreatedBy())
                                    .lastModifiedBy(dto.getLastModifiedBy())
                                    .build();
                                entity.getFeatureValueMappings().add(valueMapping);
                            }
                        });
                    }
                }
            });
        }

        return entity;
    }
}
