package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, ProductFeatureValueMapper.class})
public interface ProductFeatureMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "templateId", source = "template.id"),
        @Mapping(target = "unitId", source = "unit.id"),
        @Mapping(target = "metadata", qualifiedByName = "mapJsonNodeToString"),
        @Mapping(target = "featureValues", qualifiedByName = "mapJsonNodeToString"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    @Named("toDTO")
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product.id", source = "productId"),
        @Mapping(target = "template.id", source = "templateId"),
        @Mapping(target = "unit.id", source = "unitId"),
        @Mapping(target = "metadata", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "featureValues", ignore = true),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unit", ignore = true)
    })
    ProductFeature toEntity(ProductFeatureDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ProductFeature entity, ProductFeatureDTO dto);

    List<ProductFeatureDTO> toDTOList(List<ProductFeature> entities);
    Set<ProductFeatureDTO> toDTOSet(Set<ProductFeature> entities);
}
