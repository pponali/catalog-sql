package com.scaler.mapper;

import com.scaler.dto.ProductFeatureValueDTO;
import com.scaler.entity.ProductFeatureValue;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductFeatureValueMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", expression = "java(entityProductId(entity))"),
        @Mapping(target = "featureId", expression = "java(entityFeatureId(entity))"),
        @Mapping(target = "templateId", source = "templateId"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "validationStatus", source = "validationStatus"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "validationMessage", source = "validationMessage"),
        @Mapping(target = "attributeValue", source = "attributeValues"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    ProductFeatureValueDTO toDTO(ProductFeatureValue entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "templateId", source = "templateId"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "validationStatus", source = "validationStatus"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "validationMessage", source = "validationMessage"),
        @Mapping(target = "attributeValues", source = "attributeValue"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    ProductFeatureValue toEntity(ProductFeatureValueDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "feature", ignore = true),
        @Mapping(target = "templateId", source = "templateId"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "unit", source = "unit"),
        @Mapping(target = "unitOfMeasure", source = "unitOfMeasure"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "validationStatus", source = "validationStatus"),
        @Mapping(target = "validationPattern", source = "validationPattern"),
        @Mapping(target = "validationMessage", source = "validationMessage"),
        @Mapping(target = "attributeValues", source = "attributeValue"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    void updateEntity(@MappingTarget ProductFeatureValue entity, ProductFeatureValueDTO dto);

    List<ProductFeatureValueDTO> toDTOList(List<ProductFeatureValue> entities);
    Set<ProductFeatureValueDTO> toDTOSet(Set<ProductFeatureValue> entities);
    List<ProductFeatureValue> toEntityList(List<ProductFeatureValueDTO> dtos);
    Set<ProductFeatureValue> toEntitySet(Set<ProductFeatureValueDTO> dtos);

    default UUID entityProductId(ProductFeatureValue entity) {
        return entity.getProduct() != null ? entity.getProduct().getId() : null;
    }

    default UUID entityFeatureId(ProductFeatureValue entity) {
        return entity.getFeature() != null ? entity.getFeature().getId() : null;
    }
}