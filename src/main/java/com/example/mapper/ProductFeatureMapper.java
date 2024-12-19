package com.example.mapper;

import com.example.dto.ProductFeatureDTO;
import com.example.entity.ProductFeature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductFeatureValueMapper.class})
public interface ProductFeatureMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "values", source = "values")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "templateCode", source = "template.code")
    @Mapping(target = "templateName", source = "template.name")
    @Mapping(target = "attributeType", source = "template.attributeType")
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "values", source = "values")
    ProductFeature toEntity(ProductFeatureDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "values", source = "values")
    void updateEntityFromDTO(ProductFeatureDTO dto, @MappingTarget ProductFeature entity);
}
