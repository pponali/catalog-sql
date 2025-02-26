package com.scaler.mapper;

import com.scaler.dto.ProductFeatureMappingDTO;
import com.scaler.entity.ProductFeatureMapping;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, ProductFeatureValueMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(ProductFeatureMappingMapperDecorator.class)
public interface ProductFeatureMappingMapper extends JsonNodeMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "featureId", source = "feature.id"),
        @Mapping(target = "displayOrder", source = "displayOrder"),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "enabled", source = "enabled"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonNodeToString"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "featureValues", ignore = true)
    })
    ProductFeatureMappingDTO toDTO(ProductFeatureMapping entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "displayOrder", source = "displayOrder"),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "enabled", source = "enabled"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonStringToJsonNode"),
        @Mapping(target = "featureValues", source = "featureValues")
    })
    ProductFeatureMapping toEntity(ProductFeatureMappingDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "displayOrder", source = "displayOrder"),
        @Mapping(target = "visible", source = "visible"),
        @Mapping(target = "enabled", source = "enabled"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonStringToJsonNode"),
        @Mapping(target = "featureValues", source = "featureValues")
    })
    void updateEntity(@MappingTarget ProductFeatureMapping entity, ProductFeatureMappingDTO dto);

    List<ProductFeatureMappingDTO> toDTOList(List<ProductFeatureMapping> entities);
    Set<ProductFeatureMappingDTO> toDTOSet(Set<ProductFeatureMapping> entities);
}
