package com.scaler.mapper;

import com.scaler.dto.FeatureTemplateDTO;
import com.scaler.entity.FeatureTemplate;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, MapperUtils.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FeatureTemplateMapper extends JsonNodeMapper{
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "featureType", source = "featureType")
    @Mapping(target = "dataType", source = "dataType")
    @Mapping(target = "inputType", source = "inputType")
    @Mapping(target = "validationPattern", source = "validationPattern")
    @Mapping(target = "minValue", source = "minValue")
    @Mapping(target = "maxValue", source = "maxValue")
    @Mapping(target = "allowedValues", source = "allowedValues")
    @Mapping(target = "defaultValue", source = "defaultValue")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "required", source = "required")
    @Mapping(target = "filterable", source = "filterable")
    @Mapping(target = "hidden", source = "hidden")
    @Mapping(target = "multiValued", source = "multiValued")
    @Mapping(target = "searchable", source = "searchable")
    @Mapping(target = "comparable", source = "comparable")
    @Mapping(target = "visible", source = "visible")
    @Mapping(target = "editable", source = "editable")
    @Mapping(target = "metadata", source = "metadata" , qualifiedByName = "jsonNodeToString")
    FeatureTemplateDTO toDTO(FeatureTemplate entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "featureType", source = "featureType")
    @Mapping(target = "dataType", source = "dataType")
    @Mapping(target = "inputType", source = "inputType")
    @Mapping(target = "validationPattern", source = "validationPattern")
    @Mapping(target = "minValue", source = "minValue")
    @Mapping(target = "maxValue", source = "maxValue")
    @Mapping(target = "allowedValues", source = "allowedValues")
    @Mapping(target = "defaultValue", source = "defaultValue")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "required", source = "required")
    @Mapping(target = "filterable", source = "filterable")
    @Mapping(target = "hidden", source = "hidden")
    @Mapping(target = "multiValued", source = "multiValued")
    @Mapping(target = "searchable", source = "searchable")
    @Mapping(target = "comparable", source = "comparable")
    @Mapping(target = "visible", source = "visible")
    @Mapping(target = "editable", source = "editable")
    @Mapping(target = "metadata", source = "metadata" , qualifiedByName = "jsonStringToJsonNode")
    FeatureTemplate toEntity(FeatureTemplateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(FeatureTemplateDTO dto, @MappingTarget FeatureTemplate entity);
}
