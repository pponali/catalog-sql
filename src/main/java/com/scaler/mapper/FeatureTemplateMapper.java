package com.scaler.mapper;

import com.scaler.dto.FeatureTemplateDTO;
import com.scaler.entity.FeatureTemplate;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeatureTemplateMapper {
    
    FeatureTemplateMapper INSTANCE = Mappers.getMapper(FeatureTemplateMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "lastModifiedAt", source = "lastModifiedDate")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    FeatureTemplateDTO toDTO(FeatureTemplate entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "createdDate", source = "createdAt")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    FeatureTemplate toEntity(FeatureTemplateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(FeatureTemplateDTO dto, @MappingTarget FeatureTemplate entity);
}
