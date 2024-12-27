package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.UnitOfMeasure;
import org.mapstruct.*;

import java.util.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductFeatureMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "featureType", source = "featureType")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "required", source = "required")
    @Mapping(target = "validationPattern", source = "validationPattern")
    @Mapping(target = "minValue", source = "minValue")
    @Mapping(target = "maxValue", source = "maxValue")
    @Mapping(target = "allowedValues", source = "allowedValues")
    @Mapping(target = "values", ignore = true)
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "featureType", source = "featureType")
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "required", source = "required")
    @Mapping(target = "validationPattern", source = "validationPattern")
    @Mapping(target = "minValue", source = "minValue")
    @Mapping(target = "maxValue", source = "maxValue")
    @Mapping(target = "allowedValues", source = "allowedValues")
    @Mapping(target = "values", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "code", expression = "java(generateCode(dto))")
    ProductFeature toEntity(ProductFeatureDTO dto);

    List<ProductFeatureDTO> toDto(List<ProductFeature> entities);

    List<ProductFeature> toEntity(List<ProductFeatureDTO> dtos);

    @AfterMapping
    default void afterToEntity(@MappingTarget ProductFeature feature, ProductFeatureDTO dto) {
        if (dto.getUnitId() != null) {
            UnitOfMeasure unit = new UnitOfMeasure();
            unit.setId(dto.getUnitId());
            feature.setUnit(unit);
        }
    }

    default String generateCode(ProductFeatureDTO dto) {
        return dto.getName().toLowerCase().replaceAll("\\s+", "_");
    }
}
