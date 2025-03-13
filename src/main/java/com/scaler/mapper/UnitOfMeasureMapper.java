package com.scaler.mapper;

import com.scaler.dto.UnitOfMeasureDTO;
import com.scaler.entity.Unit;
import com.scaler.entity.UnitOfMeasure;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UnitOfMeasureMapper {
    
    UnitOfMeasureMapper INSTANCE = Mappers.getMapper(UnitOfMeasureMapper.class);

    @Mapping(target = "baseUnit", source = "baseUnit.code")
    UnitOfMeasureDTO toDTO(UnitOfMeasure unitOfMeasure);

    @Named("toEntity")
    default UnitOfMeasure toEntity(UnitOfMeasureDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return UnitOfMeasure.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .conversionFactor(dto.getConversionFactor())
                .build();
    }

    @Mapping(target = "baseUnit", ignore = true)
    void updateEntityFromDTO(UnitOfMeasureDTO dto, @MappingTarget UnitOfMeasure entity);

    default String mapUnitToString(Unit unit) {
        return unit != null ? unit.getCode() : null;
    }
}
