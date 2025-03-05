package com.scaler.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scaler.dto.ProductFeatureValueMappingDTO;
import com.scaler.entity.ProductFeatureValueMapping;

@Mapper(componentModel = "spring", uses = {ProductFeatureMapper.class, ProductFeatureValueMapper.class})
public interface ProductFeatureValueMappingMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "featureId", source = "feature.id")
    @Mapping(target = "featureName", source = "feature.name")
    @Mapping(target = "featureValueId", source = "featureValue.id")
    ProductFeatureValueMappingDTO toDto(ProductFeatureValueMapping entity);

    List<ProductFeatureValueMappingDTO> toDtoList(List<ProductFeatureValueMapping> entities);
}
