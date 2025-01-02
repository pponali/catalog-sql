package com.scaler.mapper;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, ProductFeatureMapper.class, ProductAttributeMapper.class},
        imports = {Collectors.class})
public interface ProductMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "businessId", source = "business.id"),
        @Mapping(target = "catalogId", source = "catalog.id"),
        @Mapping(target = "unitOfMeasureId", source = "unitOfMeasure.id"),
        @Mapping(target = "metadata", ignore = true),
        @Mapping(target = "features", ignore = true),
        @Mapping(target = "features", ignore = true),
        @Mapping(target = "categoryIds", expression = "java(entity.getCategories().stream().map(category -> category.getId()).collect(Collectors.toSet()))"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime")
    })
    ProductDTO toDTO(Product entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "business", ignore = true),
        @Mapping(target = "catalog", ignore = true),
        @Mapping(target = "unitOfMeasure", ignore = true),
        @Mapping(target = "categories", ignore = true),
        @Mapping(target = "features", ignore = true),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    Product toEntity(ProductDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Product entity, ProductDTO dto);

    List<ProductDTO> toDTOList(List<Product> entities);
    Set<ProductDTO> toDTOSet(Set<Product> entities);
}