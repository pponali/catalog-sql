package com.scaler.mapper;

import com.scaler.dto.CategoryFeatureTemplateDTO;
import com.scaler.entity.CategoryFeatureTemplate;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class, FeatureTemplateMapper.class})
public interface CategoryFeatureTemplateMapper {
    
    CategoryFeatureTemplateMapper INSTANCE = Mappers.getMapper(CategoryFeatureTemplateMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "categoryId", source = "category.id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "type", source = "featureType"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    CategoryFeatureTemplateDTO toDTO(CategoryFeatureTemplate entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "featureType", source = "type"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    CategoryFeatureTemplate toEntity(CategoryFeatureTemplateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "features", ignore = true)
    })
    void updateEntityFromDTO(CategoryFeatureTemplateDTO dto, @MappingTarget CategoryFeatureTemplate entity);

    List<CategoryFeatureTemplateDTO> toDTOList(List<CategoryFeatureTemplate> entities);
    Set<CategoryFeatureTemplateDTO> toDTOSet(Set<CategoryFeatureTemplate> entities);
}
