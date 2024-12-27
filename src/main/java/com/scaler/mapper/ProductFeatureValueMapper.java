package com.scaler.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductFeatureMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class ProductFeatureValueMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "feature", source = "feature")
    @Mapping(target = "attributeValue", expression = "java(mapAttributeValues(dto))")
    @Mapping(target = "validationPattern", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "createdBy", constant = "system")
    @Mapping(target = "lastModifiedBy", constant = "system")
    public abstract ProductFeatureValue toEntity(ProductFeatureValueDTO dto);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "feature", source = "feature")
    @Mapping(target = "value", expression = "java(entity.getValueAsString())")
    public abstract ProductFeatureValueDTO toDTO(ProductFeatureValue entity);

    @Named("mapAttributeValues")
    protected JsonNode mapAttributeValues(ProductFeatureValueDTO dto) {
        try {
            if (dto.getValue() != null) {
                return objectMapper.readTree(dto.getValue());
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping attribute values", e);
        }
    }

    @AfterMapping
    protected void afterToEntity(@MappingTarget ProductFeatureValue entity, ProductFeatureValueDTO dto) {
        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
        if (dto.getUnit() != null) {
            entity.setUnit(dto.getUnit());
        }
        if (dto.getUnitOfMeasure() != null) {
            entity.setUnitOfMeasure(dto.getUnitOfMeasure());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getValidationStatus() != null) {
            entity.setValidationStatus(dto.getValidationStatus());
        }
        if (dto.getValidationMessage() != null) {
            entity.setValidationMessage(dto.getValidationMessage());
        }
        entity.setCreatedBy("system");
        entity.setLastModifiedBy("system");
    }

    public abstract List<ProductFeatureValueDTO> toDto(List<ProductFeatureValue> entities);

    public abstract List<ProductFeatureValue> toEntity(List<ProductFeatureValueDTO> dtos);
}