package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.UnitOfMeasure;
import org.mapstruct.*;

import java.util.*;

@Mapper(componentModel = "spring", uses = {ProductFeatureValueMapper.class})
public interface ProductFeatureMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "featureType")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "unitToString")
    @Mapping(target = "required", source = "required")
    @Mapping(target = "validationPattern", source = "validationPattern")
    @Mapping(target = "minValue", source = "minValue")
    @Mapping(target = "maxValue", source = "maxValue")
    @Mapping(target = "allowedValues", source = "allowedValues", qualifiedByName = "stringToList")
    @Mapping(target = "values", source = "values")
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "featureType", source = "type")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "stringToUnit")
    @Mapping(target = "required", source = "required")
    @Mapping(target = "validationPattern", source = "validationPattern")
    @Mapping(target = "minValue", source = "minValue")
    @Mapping(target = "maxValue", source = "maxValue")
    @Mapping(target = "allowedValues", source = "allowedValues", qualifiedByName = "listToString")
    @Mapping(target = "values", source = "values")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "code", expression = "java(generateCode(dto))")
    ProductFeature toEntity(ProductFeatureDTO dto);

    List<ProductFeatureDTO> toDto(List<ProductFeature> entities);

    List<ProductFeature> toEntity(List<ProductFeatureDTO> dtos);

    @Named("unitToString")
    default String unitToString(UnitOfMeasure unit) {
        return unit != null ? unit.getCode() : null;
    }

    @Named("stringToUnit")
    default UnitOfMeasure stringToUnit(String unitCode) {
        if (unitCode == null) {
            return null;
        }
        return UnitOfMeasure.builder().code(unitCode).build();
    }

    @Named("stringToList")
    default List<String> stringToList(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(value.split(","));
    }

    @Named("listToString")
    default String listToString(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return String.join(",", values);
    }

    default String generateCode(ProductFeatureDTO dto) {
        return dto.getName().toLowerCase().replaceAll("\\s+", "_");
    }
}
