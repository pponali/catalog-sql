package com.scaler.mapper;

import com.scaler.dto.ValidationResultDTO;
import com.scaler.entity.ValidationResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ValidationResultMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "entityId", source = "entityId"),
        @Mapping(target = "entityType", source = "entityType"),
        @Mapping(target = "fieldName", source = "fieldName"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "errors", source = "errors")
    })
    ValidationResultDTO toDTO(ValidationResult entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "entityId", source = "entityId"),
        @Mapping(target = "entityType", source = "entityType"),
        @Mapping(target = "fieldName", source = "fieldName"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "errors", source = "errors")
    })
    ValidationResult toEntity(ValidationResultDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ValidationResultDTO dto, @MappingTarget ValidationResult entity);
}
