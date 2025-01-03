package com.scaler.mapper;

import com.scaler.dto.CategoryFeatureTemplateDTO;
import com.scaler.entity.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class, FeatureTemplateMapper.class})
public interface CategoryFeatureTemplateMapper extends JsonNodeMapper {

    CategoryFeatureTemplateMapper INSTANCE = Mappers.getMapper(CategoryFeatureTemplateMapper.class);

    @Mappings({
        @Mapping(target = "categoryId", source = "category.id"),
        @Mapping(target = "templateId", source = "template.id"),
        @Mapping(target = "unitId", source = "unit.id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "featureType", source = "featureType"),
        @Mapping(target = "createdDate", source = "createdDate"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    CategoryFeatureTemplateDTO toDTO(CategoryFeatureTemplate entity);

    @Mappings({
        @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory"),
        @Mapping(target = "template", source = "templateId", qualifiedByName = "mapTemplate"),
        @Mapping(target = "unit", source = "unitId", qualifiedByName = "mapUnit"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "featureType", source = "featureType"),
        @Mapping(target = "createdDate", source = "createdDate"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    CategoryFeatureTemplate toEntity(CategoryFeatureTemplateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory"),
        @Mapping(target = "template", source = "templateId", qualifiedByName = "mapTemplate"),
        @Mapping(target = "unit", source = "unitId", qualifiedByName = "mapUnit"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "featureType", source = "featureType"),
        @Mapping(target = "createdDate", source = "createdDate"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
            @Mapping(target = "required", source = "required", ignore = true),

        @Mapping(target = "searchable", source = "searchable", ignore = true),

            @Mapping(target = "editable", source = "editable", ignore = true),

        @Mapping(target = "features", ignore = true)
    })
    void updateEntityFromDTO(CategoryFeatureTemplateDTO dto, @MappingTarget CategoryFeatureTemplate entity);

    List<CategoryFeatureTemplateDTO> toDTOList(List<CategoryFeatureTemplate> entities);
    Set<CategoryFeatureTemplateDTO> toDTOSet(Set<CategoryFeatureTemplate> entities);

    @Named("mapCategory")
    default Category mapCategory(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("mapTemplate")
    default FeatureTemplate mapTemplate(UUID templateId) {
        if (templateId == null) {
            return null;
        }
        FeatureTemplate template = new FeatureTemplate();
        template.setId(templateId);
        return template;
    }

    @Named("mapUnit")
    default UnitOfMeasure mapUnit(UUID unitId) {
        if (unitId == null) {
            return null;
        }
        UnitOfMeasure unit = new UnitOfMeasure();
        unit.setId(unitId);
        return unit;
    }
}
