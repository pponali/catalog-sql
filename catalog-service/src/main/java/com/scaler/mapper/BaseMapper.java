package com.scaler.mapper;

import com.scaler.dto.BaseDTO;
import com.scaler.entity.BaseEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.UUID;

public interface BaseMapper extends JsonNodeMapper {

    @Named("uuidToLong")
    default Long uuidToLong(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return uuid.getLeastSignificantBits();
    }

    @Named("longToUuid")
    default UUID longToUuid(Long value) {
        if (value == null) {
            return null;
        }
        return new UUID(0, value);
    }

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "createdDate", source = "createdDate"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    BaseDTO toBaseDTO(BaseEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "createdDate", source = "createdDate"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    BaseEntity toBaseEntity(BaseDTO dto);
}
