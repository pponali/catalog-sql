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
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "categoryId", source = "category.id"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "featureType", source = "featureType"),
            @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonNodeToString")
    })
    CategoryFeatureTemplateDTO toDTO(CategoryFeatureTemplate entity);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "featureType", source = "featureType"),
            @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonStringToJsonNode")
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
