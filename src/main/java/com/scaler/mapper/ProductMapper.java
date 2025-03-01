package com.scaler.mapper;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {SellerProductMapper.class, CommonMapper.class, MapperUtils.class, ProductFeatureMapper.class, ProductFeatureValueMapper.class})
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper  {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "merchantId", source = "merchant.id"),
            @Mapping(target = "catalogId", source = "catalog.id"),
            @Mapping(target = "categoryIds", expression = "java(entity.getProductCategories() != null ? entity.getProductCategories().stream().map(pc -> pc.getCategory().getId()).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>())"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "features", ignore = true)
    })
    ProductDTO toDTO(Product entity);

    @Mappings({
            @Mapping(target = "merchant", ignore = true),
            @Mapping(target = "catalog", ignore = true),
            @Mapping(target = "productCategories", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "status", source = "status")
    })
    Product toEntity(ProductDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "merchant", ignore = true),
            @Mapping(target = "catalog", ignore = true),
            @Mapping(target = "productCategories", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "status", source = "status")
    })
    void updateEntity(@MappingTarget Product entity, ProductDTO dto);
}