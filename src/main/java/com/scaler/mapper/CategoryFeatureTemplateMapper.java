package com.scaler.mapper;

import com.scaler.dto.CategoryFeatureTemplateDTO;
import com.scaler.entity.CategoryFeatureTemplate;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductFeatureMapper.class})
public interface CategoryFeatureTemplateMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "categoryId", source = "category.id"),
        @Mapping(target = "unitId", source = "unit.id"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
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
        @Mapping(target = "mandatory", source = "mandatory"),
        @Mapping(target = "multiValued", source = "multiValued"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "features", source = "features")
    })
    CategoryFeatureTemplateDTO toDTO(CategoryFeatureTemplate entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "unit", ignore = true),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
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
        @Mapping(target = "mandatory", source = "mandatory"),
        @Mapping(target = "multiValued", source = "multiValued"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "features", source = "features")
    })
    CategoryFeatureTemplate toEntity(CategoryFeatureTemplateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CategoryFeatureTemplate entity, CategoryFeatureTemplateDTO dto);

    List<CategoryFeatureTemplateDTO> toDTOList(List<CategoryFeatureTemplate> entities);
    Set<CategoryFeatureTemplateDTO> toDTOSet(Set<CategoryFeatureTemplate> entities);
}
