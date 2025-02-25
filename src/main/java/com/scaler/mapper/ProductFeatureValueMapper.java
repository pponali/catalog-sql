package com.scaler.mapper;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductFeatureValueMapper extends JsonNodeMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "featureId", source = "feature.id"),
        @Mapping(target = "featureTemplateId", source = "templateId"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "metadata", source = "metadata" , qualifiedByName = "jsonNodeToString"),
        @Mapping(target = "attributeValues", source = "attributeValues", qualifiedByName = "jsonNodeToString"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true)
    })
    ProductFeatureValueDTO toDTO(ProductFeatureValue entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
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
        @Mapping(target = "product", ignore = true),
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