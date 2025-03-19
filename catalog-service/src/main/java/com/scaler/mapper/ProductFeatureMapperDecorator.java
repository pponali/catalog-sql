package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.dto.ProductFeatureWithValuesDTO;
import com.scaler.entity.ProductFeature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProductFeatureMapperDecorator implements ProductFeatureMapper {

    @Autowired
    @Qualifier("delegate")
    private ProductFeatureMapper delegate;

    @Override
    public ProductFeatureDTO toDTO(ProductFeature entity) {
        if (entity == null) {
            return null;
        }
        return delegate.toDTO(entity);
    }

    public ProductFeatureWithValuesDTO toDTOWithValues(ProductFeature entity, Set<com.scaler.dto.ProductFeatureValueDTO> values) {
        ProductFeatureDTO basicDTO = toDTO(entity);
        if (basicDTO == null) {
            return null;
        }

        ProductFeatureWithValuesDTO dto = new ProductFeatureWithValuesDTO();
        BeanUtils.copyProperties(basicDTO, dto);
        dto.setValues(values != null ? values : new HashSet<>());
        return dto;
    }

    @Override
    public ProductFeature toEntity(ProductFeatureDTO dto) {
        return delegate.toEntity(dto);
    }

    @Override
    public List<ProductFeatureDTO> toDTOList(List<ProductFeature> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<ProductFeatureDTO> toDTOSet(Set<ProductFeature> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }
}
