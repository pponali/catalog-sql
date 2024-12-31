package com.scaler.mapper;

import com.scaler.dto.UnitOfMeasureDTO;
import com.scaler.entity.UnitOfMeasure;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UnitOfMeasureMapper {
    
    UnitOfMeasureMapper INSTANCE = Mappers.getMapper(UnitOfMeasureMapper.class);

    UnitOfMeasureDTO toDTO(UnitOfMeasure unitOfMeasure);

    UnitOfMeasure toEntity(UnitOfMeasureDTO dto);

    void updateEntityFromDTO(UnitOfMeasureDTO dto, @MappingTarget UnitOfMeasure entity);
}
