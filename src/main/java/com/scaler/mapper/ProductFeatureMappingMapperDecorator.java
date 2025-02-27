package com.scaler.mapper;

import com.scaler.dto.ProductFeatureMappingDTO;
import com.scaler.entity.ProductFeatureMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProductFeatureMappingMapperDecorator implements ProductFeatureMappingMapper {

    @Autowired
    @Qualifier("delegate")
    private ProductFeatureMappingMapper delegate;

    @Autowired
    private ProductFeatureValueMapper featureValueMapper;

    @Override
    public ProductFeatureMappingDTO toDTO(ProductFeatureMapping entity) {
        ProductFeatureMappingDTO dto = delegate.toDTO(entity);
        if (dto == null || entity == null) {
            return dto;
        }

        // Map feature values separately to avoid circular dependency
        if (entity.getFeatureValues() != null) {
            dto.setFeatureValues(entity.getFeatureValues().stream()
                .map(value -> {
                    var valueDTO = featureValueMapper.toDTO(value);
                    if (valueDTO != null) {
                        valueDTO.setFeatureMappingId(null); // Break circular reference
                    }
                    return valueDTO;
                })
                .collect(Collectors.toSet()));
        } else {
            dto.setFeatureValues(new HashSet<>());
        }

        return dto;
    }

    @Override
    public ProductFeatureMapping toEntity(ProductFeatureMappingDTO dto) {
        ProductFeatureMapping entity = delegate.toEntity(dto);
        if (entity == null) {
            return null;
        }
        return entity;
    }

    @Override
    public List<ProductFeatureMappingDTO> toDTOList(List<ProductFeatureMapping> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<ProductFeatureMappingDTO> toDTOSet(Set<ProductFeatureMapping> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
