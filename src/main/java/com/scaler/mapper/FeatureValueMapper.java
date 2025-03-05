package com.scaler.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.fasterxml.jackson.databind.JsonNode;
import com.scaler.dto.FeatureValueDTO;
import com.scaler.entity.ProductFeatureValueMapping;

@Mapper(componentModel = "spring")
public interface FeatureValueMapper {

    @Mapping(target = "featureId", source = "feature.id")
    @Mapping(target = "featureName", source = "feature.name")
    @Mapping(target = "featureCode", source = "feature.code")
    @Mapping(target = "templateId", source = "feature.template.id")
    @Mapping(target = "templateName", source = "feature.template.name")
    @Mapping(target = "value", source = "featureValue.attributeValues", qualifiedByName = "jsonNodeToString")
    @Mapping(target = "createdAt", source = "featureValue.createdDate")
    @Mapping(target = "updatedAt", source = "featureValue.lastModifiedDate")
    @Mapping(target = "createdBy", source = "featureValue.createdBy")
    @Mapping(target = "updatedBy", source = "featureValue.lastModifiedBy")
    FeatureValueDTO toDto(ProductFeatureValueMapping mapping);

    List<FeatureValueDTO> toDtoList(List<ProductFeatureValueMapping> mappings);
    
    @Named("jsonNodeToString")
    default String jsonNodeToString(JsonNode node) {
        if (node == null) {
            return null;
        }
        return node.toString();
    }
}
