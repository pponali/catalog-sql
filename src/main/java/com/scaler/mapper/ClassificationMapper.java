package com.scaler.mapper;

import com.scaler.dto.ClassificationAttributeDTO;
import com.scaler.dto.ClassificationClassDTO;
import com.scaler.entity.ClassificationAttribute;
import com.scaler.entity.ClassificationClass;
import org.springframework.stereotype.Component;

@Component
public class ClassificationMapper {

    public ClassificationAttributeDTO toDTO(ClassificationAttribute entity) {
        if (entity == null) {
            return null;
        }
        
        ClassificationAttributeDTO dto = new ClassificationAttributeDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return dto;
    }

    public ClassificationAttribute toEntity(ClassificationAttributeDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ClassificationAttribute entity = new ClassificationAttribute();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        return entity;
    }

    public ClassificationClassDTO toDTO(ClassificationClass entity) {
        if (entity == null) {
            return null;
        }
        
        ClassificationClassDTO dto = new ClassificationClassDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        return dto;
    }

    public ClassificationClass toEntity(ClassificationClassDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ClassificationClass entity = new ClassificationClass();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        return entity;
    }
}
