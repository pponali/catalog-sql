package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductFeatureValueMapper.class})
public interface ProductFeatureMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "templateId", source = "template.id"),
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
        @Mapping(target = "unitId", source = "unit.id"),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "editable", source = "editable"),
        @Mapping(target = "searchable", source = "searchable"),
        @Mapping(target = "comparable", source = "comparable"),
        @Mapping(target = "required", source = "required"),
        @Mapping(target = "multiValued", source = "multiValued"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "values", source = "values")
    })
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "template", ignore = true),
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
        @Mapping(target = "unit", ignore = true),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "editable", source = "editable"),
        @Mapping(target = "searchable", source = "searchable"),
        @Mapping(target = "comparable", source = "comparable"),
        @Mapping(target = "required", source = "required"),
        @Mapping(target = "multiValued", source = "multiValued"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "values", ignore = true),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    ProductFeature toEntity(ProductFeatureDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ProductFeature entity, ProductFeatureDTO dto);

    List<ProductFeatureDTO> toDTOList(List<ProductFeature> entities);

    Set<ProductFeatureDTO> toDTOSet(Set<ProductFeature> entities);
}
