package com.scaler.mapper;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductFeatureMapper.class, CommonMapper.class})
public interface ProductMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "businessId", source = "business.id"),
        @Mapping(target = "catalogId", source = "catalog.id"),
        @Mapping(target = "categoryIds", expression = "java(entity.getCategories().stream().map(category -> category.getId()).collect(java.util.stream.Collectors.toSet()))"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "features", source = "features"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime")
    })
    ProductDTO toDTO(Product entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "business", ignore = true),
        @Mapping(target = "catalog", ignore = true),
        @Mapping(target = "categories", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "features", source = "features"),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime")
    })
    Product toEntity(ProductDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "business", ignore = true),
        @Mapping(target = "catalog", ignore = true),
        @Mapping(target = "categories", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "features", source = "features")
    })
    void updateEntity(@MappingTarget Product entity, ProductDTO dto);
}