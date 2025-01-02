package com.scaler.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class})
public interface ProductFeatureValueMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "featureId", source = "feature.id"),
        @Mapping(target = "featureTemplateId", source = "templateId"),
        @Mapping(target = "metadata", qualifiedByName = "mapJsonNodeToString"),
        @Mapping(target = "attributeValues", qualifiedByName = "mapJsonNodeToString"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime")
    })
    @Named("toDTO")
    ProductFeatureValueDTO toDTO(ProductFeatureValue entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product.id", source = "productId"),
        @Mapping(target = "feature.id", source = "featureId"),
        @Mapping(target = "templateId", source = "featureTemplateId"),
        @Mapping(target = "metadata", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "attributeValues", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true)
    })
    @Named("toEntity")
    ProductFeatureValue toEntity(ProductFeatureValueDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "templateId", source = "featureTemplateId"),
        @Mapping(target = "attributeValues", expression = "java(MapperUtils.mapStringToJsonNode(dto.getAttributeValues()))")
    })
    void updateEntityFromDTO(ProductFeatureValueDTO dto, @MappingTarget ProductFeatureValue entity);

    List<ProductFeatureValueDTO> toDTOList(List<ProductFeatureValue> entities);
    Set<ProductFeatureValueDTO> toDTOSet(Set<ProductFeatureValue> entities);
}