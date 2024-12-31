package com.scaler.mapper;

import com.scaler.dto.ClassificationAttributeDTO;
import com.scaler.dto.ClassificationClassDTO;
import com.scaler.entity.ClassificationAttribute;
import com.scaler.entity.ClassificationClass;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassificationMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    ClassificationAttributeDTO toDTO(ClassificationAttribute entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    ClassificationAttribute toEntity(ClassificationAttributeDTO dto);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "parentId", source = "parent.id"),
        @Mapping(target = "inheritFeatures", source = "inheritFeatures"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "businessId", source = "business.id"),
        @Mapping(target = "catalogId", source = "catalog.id")
    })
    ClassificationClassDTO toDTO(ClassificationClass entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "parent", ignore = true),
        @Mapping(target = "inheritFeatures", source = "inheritFeatures"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "business", ignore = true),
        @Mapping(target = "catalog", ignore = true),
        @Mapping(target = "attributeAssignments", ignore = true)
    })
    ClassificationClass toEntity(ClassificationClassDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ClassificationClass entity, ClassificationClassDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ClassificationAttribute entity, ClassificationAttributeDTO dto);
}
