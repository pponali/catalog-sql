package com.scaler.mapper;

import com.scaler.dto.FeatureTemplateDTO;
import com.scaler.entity.FeatureTemplate;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeatureTemplateMapper {
    
    FeatureTemplateMapper INSTANCE = Mappers.getMapper(FeatureTemplateMapper.class);

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
    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "lastModifiedAt", source = "lastModifiedDate")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
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
    @Mapping(target = "createdDate", source = "createdAt")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    FeatureTemplate toEntity(FeatureTemplateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(FeatureTemplateDTO dto, @MappingTarget FeatureTemplate entity);
}
