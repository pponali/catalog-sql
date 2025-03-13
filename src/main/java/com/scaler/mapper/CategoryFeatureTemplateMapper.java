package com.scaler.mapper;

import com.scaler.dto.CategoryFeatureTemplateDTO;
import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class, FeatureTemplateMapper.class, CommonMapper.class, MapperUtils.class}
        )
public interface CategoryFeatureTemplateMapper extends JsonNodeMapper {


    @Mappings({
            @Mapping(target = "categoryId", source = "category.id"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "attributeType", source = "attributeType"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "featureType", source = "featureType"),
            @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonNodeToString")
    })
    CategoryFeatureTemplateDTO toDTO(CategoryFeatureTemplate entity);

    @Named("toEntity")
    default CategoryFeatureTemplate toEntity(CategoryFeatureTemplateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Convert String featureType to FeatureType enum
        com.scaler.entity.FeatureType featureTypeEnum = null;
        if (dto.getFeatureType() != null && !dto.getFeatureType().isEmpty()) {
            try {
                featureTypeEnum = com.scaler.entity.FeatureType.valueOf(dto.getFeatureType());
            } catch (IllegalArgumentException e) {
                // Default to STRING if invalid
                featureTypeEnum = com.scaler.entity.FeatureType.STRING;
            }
        }
        
        return CategoryFeatureTemplate.builder()
                .code(dto.getCode())
                .attributeType(dto.getAttributeType())
                .name(dto.getName())
                .description(dto.getDescription())
                .featureType(featureTypeEnum)
                .metadata(MapperUtils.mapStringToJsonNode(dto.getMetadata()))
                .build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "features", ignore = true)
    })
    void updateEntityFromDTO(CategoryFeatureTemplateDTO dto, @MappingTarget CategoryFeatureTemplate entity);

    List<CategoryFeatureTemplateDTO> toDTOList(List<CategoryFeatureTemplate> entities);
    Set<CategoryFeatureTemplateDTO> toDTOSet(Set<CategoryFeatureTemplate> entities);
}
