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

    @Mapping(target = "baseUnit", ignore = true)
    UnitOfMeasure toEntity(UnitOfMeasureDTO dto);

    @Mapping(target = "baseUnit", ignore = true)
    void updateEntityFromDTO(UnitOfMeasureDTO dto, @MappingTarget UnitOfMeasure entity);

    default String mapUnitToString(Unit unit) {
        return unit != null ? unit.getCode() : null;
    }
}
