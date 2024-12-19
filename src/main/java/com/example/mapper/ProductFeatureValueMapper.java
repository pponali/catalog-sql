package com.example.mapper;

import com.example.dto.ProductFeatureValueDTO;
import com.example.entity.ProductFeatureValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductFeatureValueMapper {
    
    ProductFeatureValueMapper INSTANCE = Mappers.getMapper(ProductFeatureValueMapper.class);

    @Mapping(target = "featureId", source = "feature.id")
    ProductFeatureValueDTO toDTO(ProductFeatureValue value);

    @Mapping(target = "feature", ignore = true)
    ProductFeatureValue toEntity(ProductFeatureValueDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "feature", ignore = true)
    void updateEntityFromDTO(ProductFeatureValueDTO dto, @MappingTarget ProductFeatureValue entity);
}
