package com.example.mapper;

import com.example.dto.ProductFeatureValueDTO;
import com.example.entity.ProductFeatureValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductFeatureValueMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "unit", source = "unit")
    ProductFeatureValueDTO toDTO(ProductFeatureValue entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productFeature", ignore = true)
    ProductFeatureValue toEntity(ProductFeatureValueDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productFeature", ignore = true)
    void updateEntityFromDTO(ProductFeatureValueDTO dto, @MappingTarget ProductFeatureValue entity);
}
