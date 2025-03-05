package com.scaler.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.scaler.dto.PlatformDTO;
import com.scaler.entity.Platform;

@Mapper(componentModel = "spring")
public interface PlatformMapper {

    @Mapping(target = "productCount", ignore = true)
    PlatformDTO toDto(Platform entity);

    List<PlatformDTO> toDtoList(List<Platform> entities);
    
    Platform toEntity(PlatformDTO dto);
}
