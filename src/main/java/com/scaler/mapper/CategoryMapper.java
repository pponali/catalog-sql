package com.scaler.mapper;

import com.scaler.dto.CategoryDTO;
import com.scaler.entity.Category;
import com.scaler.entity.Product;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryFeatureTemplateMapper.class})
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "businessId", source = "merchant.id"),
        @Mapping(target = "catalogId", source = "catalog.id"),
        @Mapping(target = "parentId", source = "parent.id"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "children", source = "children"),
        @Mapping(target = "templates", source = "templates"),
        @Mapping(target = "productIds", expression = "java(getProductIds(entity.getProducts()))")
    })
    CategoryDTO toDTO(Category entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "merchant", ignore = true),
        @Mapping(target = "catalog", ignore = true),
        @Mapping(target = "parent", ignore = true),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "children", ignore = true),
        @Mapping(target = "templates", ignore = true),
        @Mapping(target = "products", ignore = true)
    })
    Category toEntity(CategoryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Category entity, CategoryDTO dto);

    List<CategoryDTO> toDTOList(List<Category> entities);

    default Set<UUID> getProductIds(Set<Product> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }
}
