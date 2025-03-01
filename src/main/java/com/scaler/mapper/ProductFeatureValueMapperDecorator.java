package com.scaler.mapper;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.repository.ProductFeatureRepository;
import com.scaler.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProductFeatureValueMapperDecorator implements ProductFeatureValueMapper {

    @Autowired
    @Qualifier("delegate")
    private ProductFeatureValueMapper delegate;

    @Autowired
    private ProductFeatureRepository productFeatureRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductFeatureValueDTO toDTO(ProductFeatureValue entity) {
        if (entity == null) {
            return null;
        }
        ProductFeatureValueDTO dto = delegate.toDTO(entity);
        if (entity.getFeature() != null && entity.getFeature().getTemplate() != null) {
            dto.setFeatureTemplateId(entity.getFeature().getTemplate().getId());
        }
        return dto;
    }

    @Override
    public ProductFeatureValue toEntity(ProductFeatureValueDTO dto) {
        ProductFeatureValue entity = delegate.toEntity(dto);
        if (entity == null) {
            return null;
        }

        if (dto.getFeatureId() != null) {
            entity.setFeature(productFeatureRepository.findById(dto.getFeatureId())
                    .orElseThrow(() -> new RuntimeException("Feature not found: " + dto.getFeatureId())));
        }

        if (dto.getProductId() != null) {
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId())));
        }

        return entity;
    }

    @Override
    public List<ProductFeatureValueDTO> toDTOList(List<ProductFeatureValue> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<ProductFeatureValueDTO> toDTOSet(Set<ProductFeatureValue> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
