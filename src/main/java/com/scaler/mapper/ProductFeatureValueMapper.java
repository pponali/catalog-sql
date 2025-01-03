package com.scaler.mapper;

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
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "validationStatus", source = "validationStatus"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "validationMessage", source = "validationMessage"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "mapJsonNodeToString"),
        @Mapping(target = "attributeValues", source = "attributeValues", qualifiedByName = "mapJsonNodeToString"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime"),
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
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "validationStatus", source = "validationStatus"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "validationMessage", source = "validationMessage"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "attributeValues", source = "attributeValues", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime")
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
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "validationStatus", source = "validationStatus"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "validationMessage", source = "validationMessage"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "attributeValues", source = "attributeValues", qualifiedByName = "mapStringToJsonNode")
    })
    void updateEntity(@MappingTarget ProductFeatureValue entity, ProductFeatureValueDTO dto);

    List<ProductFeatureValueDTO> toDTOList(List<ProductFeatureValue> entities);
    Set<ProductFeatureValueDTO> toDTOSet(Set<ProductFeatureValue> entities);
}