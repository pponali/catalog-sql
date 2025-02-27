package com.scaler.mapper;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, MapperUtils.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(ProductFeatureValueMapperDecorator.class)
public interface ProductFeatureValueMapper extends JsonNodeMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "createdDate", expression = "java(entity.getCreatedDate() != null ? entity.getCreatedDate().toString() : null)"),
        @Mapping(target = "lastModifiedDate", expression = "java(entity.getLastModifiedDate() != null ? entity.getLastModifiedDate().toString() : null)"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "featureId", source = "feature.id"),
        @Mapping(target = "featureTemplateId", source = "templateId"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonNodeToString"),
        @Mapping(target = "attributeValues", source = "attributeValues", qualifiedByName = "jsonNodeToString")
    })
    ProductFeatureValueDTO toDTO(ProductFeatureValue entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "templateId", source = "featureTemplateId"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "metadata", source = "metadata" , qualifiedByName = "jsonStringToJsonNode"),
        @Mapping(target = "attributeValues", source = "attributeValues" , qualifiedByName = "jsonStringToJsonNode")
    })
    ProductFeatureValue toEntity(ProductFeatureValueDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "templateId", source = "featureTemplateId"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "metadata", source = "metadata" , qualifiedByName = "jsonStringToJsonNode"),
        @Mapping(target = "attributeValues", source = "attributeValues" , qualifiedByName = "jsonStringToJsonNode")
    })
    void updateEntity(@MappingTarget ProductFeatureValue entity, ProductFeatureValueDTO dto);

    List<ProductFeatureValueDTO> toDTOList(List<ProductFeatureValue> entities);
    Set<ProductFeatureValueDTO> toDTOSet(Set<ProductFeatureValue> entities);
}