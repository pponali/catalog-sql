package com.scaler.mapper;

import com.scaler.dto.ProductCategoryDTO;
import com.scaler.entity.ProductCategory;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class, CategoryMapper.class, MerchantMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductCategoryMapper {
    
    ProductCategoryMapper INSTANCE = Mappers.getMapper(ProductCategoryMapper.class);

    @Mappings({
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "categoryId", source = "category.id"),
        @Mapping(target = "merchantId", source = "merchant.id")
    })
    ProductCategoryDTO toDTO(ProductCategory entity);

    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "merchant", ignore = true)
    })
    ProductCategory toEntity(ProductCategoryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "merchant", ignore = true)
    })
    void updateEntityFromDTO(ProductCategoryDTO dto, @MappingTarget ProductCategory entity);

    default void updateRelationships(ProductCategory entity, ProductCategoryDTO dto) {
        if (entity != null && dto != null) {
            entity.setIsPrimary(dto.getIsPrimary());
            entity.setDisplayOrder(dto.getDisplayOrder());
            entity.setEffectiveFrom(dto.getEffectiveFrom());
            entity.setEffectiveTo(dto.getEffectiveTo());
        }
    }
}
