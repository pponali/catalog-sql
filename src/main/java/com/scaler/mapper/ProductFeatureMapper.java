package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductFeatureMapper extends JsonNodeMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "templateId", source = "template.id"),
        @Mapping(target = "unitId", source = "unit.id"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "featureValues", ignore = true)
    })
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unit", ignore = true),
        @Mapping(target = "featureValues", ignore = true),
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
