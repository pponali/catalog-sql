package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, MapperUtils.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(ProductFeatureMapperDecorator.class)
public interface ProductFeatureMapper extends JsonNodeMapper {

    ProductFeatureMapper INSTANCE = Mappers.getMapper(ProductFeatureMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "templateId", source = "template.id"),
        @Mapping(target = "unitOfMeasureId", source = "unitOfMeasure.id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "attributeType", source = "attributeType"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "minValue", source = "minValue"),
        @Mapping(target = "maxValue", source = "maxValue"),
        @Mapping(target = "allowedValues", source = "allowedValues"),
        @Mapping(target = "defaultValue", source = "defaultValue"),
        @Mapping(target = "featureType", source = "featureType"),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "editable", source = "editable"),
        @Mapping(target = "searchable", source = "searchable"),
        @Mapping(target = "comparable", source = "comparable"),
        @Mapping(target = "required", source = "required"),
        @Mapping(target = "multiValued", source = "multiValued"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonNodeToString"),
        @Mapping(target = "featureValueIds", expression = "java(entity.getFeatureValues().stream().map(fv -> fv.getId()).collect(java.util.stream.Collectors.toSet()))")
    })
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productMappings", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unitOfMeasure", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "attributeType", source = "attributeType"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "minValue", source = "minValue"),
        @Mapping(target = "maxValue", source = "maxValue"),
        @Mapping(target = "allowedValues", source = "allowedValues"),
        @Mapping(target = "defaultValue", source = "defaultValue"),
        @Mapping(target = "featureType", source = "featureType"),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "editable", source = "editable"),
        @Mapping(target = "searchable", source = "searchable"),
        @Mapping(target = "comparable", source = "comparable"),
        @Mapping(target = "required", source = "required"),
        @Mapping(target = "multiValued", source = "multiValued"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonStringToJsonNode")
    })
    ProductFeature toEntity(ProductFeatureDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "productMappings", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unitOfMeasure", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "attributeType", source = "attributeType"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "minValue", source = "minValue"),
        @Mapping(target = "maxValue", source = "maxValue"),
        @Mapping(target = "allowedValues", source = "allowedValues"),
        @Mapping(target = "defaultValue", source = "defaultValue"),
        @Mapping(target = "featureType", source = "featureType"),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "editable", source = "editable"),
        @Mapping(target = "searchable", source = "searchable"),
        @Mapping(target = "comparable", source = "comparable"),
        @Mapping(target = "required", source = "required"),
        @Mapping(target = "multiValued", source = "multiValued"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonStringToJsonNode")
    })
    void updateEntity(@MappingTarget ProductFeature entity, ProductFeatureDTO dto);

    List<ProductFeatureDTO> toDTOList(List<ProductFeature> entities);
    Set<ProductFeatureDTO> toDTOSet(Set<ProductFeature> entities);
}
