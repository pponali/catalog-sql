package com.scaler.mapper;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {SellerProductMapper.class, CommonMapper.class, MapperUtils.class, ProductFeatureMapper.class, ProductFeatureValueMapper.class})
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper extends JsonNodeMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "merchantId", source = "merchant.id"),
            @Mapping(target = "catalogId", source = "catalog.id"),
            @Mapping(target = "categoryIds", expression = "java(entity.getProductCategories() != null ? entity.getProductCategories().stream().map(pc -> pc.getCategory().getId()).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>())"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonNodeToString")
    })
    ProductDTO toDTO(Product entity);

    @Named("toEntity")
    default Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Convert String productType to ProductType enum
        com.scaler.entity.ProductType productTypeEnum = null;
        if (dto.getProductType() != null && !dto.getProductType().isEmpty()) {
            try {
                productTypeEnum = com.scaler.entity.ProductType.valueOf(dto.getProductType());
            } catch (IllegalArgumentException e) {
                // Default to SIMPLE if invalid
                productTypeEnum = com.scaler.entity.ProductType.SIMPLE;
            }
        }
        
        return Product.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .productType(productTypeEnum)
                .sku(dto.getSku())
                .price(dto.getPrice())
                .metadata(dto.getMetadata())
                .build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "merchant", ignore = true),
            @Mapping(target = "catalog", ignore = true),
            @Mapping(target = "productCategories", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonStringToJsonNode")
    })
    void updateEntity(@MappingTarget Product entity, ProductDTO dto);
}
