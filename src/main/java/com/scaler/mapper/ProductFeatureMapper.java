package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductFeatureValueMapper.class, CommonMapper.class, MapperUtils.class})
public interface ProductFeatureMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "templateId", source = "template.id"),
        @Mapping(target = "unitId", source = "unit.id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "mapJsonNodeToString"),
        @Mapping(target = "featureValues", source = "featureValues"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime")
    })
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unit", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "featureValues", source = "featureValues"),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime")
    })
    ProductFeature toEntity(ProductFeatureDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unit", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "mapStringToJsonNode"),
        @Mapping(target = "featureValues", source = "featureValues")
    })
    void updateEntity(@MappingTarget ProductFeature entity, ProductFeatureDTO dto);

    List<ProductFeatureDTO> toDTOList(List<ProductFeature> entities);
    Set<ProductFeatureDTO> toDTOSet(Set<ProductFeature> entities);
}
