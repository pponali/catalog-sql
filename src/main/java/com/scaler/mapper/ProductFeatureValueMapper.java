package com.scaler.mapper;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {MapperUtils.class})
public interface ProductFeatureValueMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "featureTemplateId", source = "templateId"),
        @Mapping(target = "attributeValues", expression = "java(MapperUtils.mapJsonNodeToString(entity.getAttributeValues()))"),
        @Mapping(target = "stringValue", source = "stringValue"),
        @Mapping(target = "numericValue", source = "numericValue"),
        @Mapping(target = "booleanValue", source = "booleanValue"),
        @Mapping(target = "createdDate", source = "createdDate"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    ProductFeatureValueDTO toDTO(ProductFeatureValue entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "templateId", source = "featureTemplateId"),
        @Mapping(target = "attributeValues", expression = "java(MapperUtils.mapStringToJsonNode(dto.getAttributeValues()))"),
        @Mapping(target = "stringValue", source = "stringValue"),
        @Mapping(target = "numericValue", source = "numericValue"),
        @Mapping(target = "booleanValue", source = "booleanValue"),
        @Mapping(target = "createdDate", source = "createdDate"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    ProductFeatureValue toEntity(ProductFeatureValueDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "templateId", source = "featureTemplateId"),
        @Mapping(target = "attributeValues", expression = "java(MapperUtils.mapStringToJsonNode(dto.getAttributeValues()))"),
        @Mapping(target = "stringValue", source = "stringValue"),
        @Mapping(target = "numericValue", source = "numericValue"),
        @Mapping(target = "booleanValue", source = "booleanValue")
    })
    void updateEntityFromDTO(ProductFeatureValueDTO dto, @MappingTarget ProductFeatureValue entity);

    List<ProductFeatureValueDTO> toDTOList(List<ProductFeatureValue> entities);
    Set<ProductFeatureValueDTO> toDTOSet(Set<ProductFeatureValue> entities);
}