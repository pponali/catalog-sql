package com.scaler.mapper;

import com.scaler.dto.ProductFeatureDTO;
import com.scaler.entity.ProductFeature;
import com.scaler.mapper.util.MapperUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductFeatureValueMapper.class, CommonMapper.class, MapperUtils.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProductFeatureMapper extends JsonNodeMapper {

    ProductFeatureMapper INSTANCE = Mappers.getMapper(ProductFeatureMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "templateId", source = "template.id"),
        @Mapping(target = "unitOfMeasureId", source = "unitOfMeasure.id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonNodeToString"),
        @Mapping(target = "featureValues", source = "featureValues")
    })
    ProductFeatureDTO toDTO(ProductFeature entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unitOfMeasure", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "metadata", source = "metadata", qualifiedByName = "jsonStringToJsonNode"),
        @Mapping(target = "featureValues", source = "featureValues")
    })
    ProductFeature toEntity(ProductFeatureDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "template", ignore = true),
        @Mapping(target = "unitOfMeasure", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "featureValues", source = "featureValues")
    })
    void updateEntity(@MappingTarget ProductFeature entity, ProductFeatureDTO dto);

    List<ProductFeatureDTO> toDTOList(List<ProductFeature> entities);
    Set<ProductFeatureDTO> toDTOSet(Set<ProductFeature> entities);
}
