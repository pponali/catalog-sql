package com.scaler.mapper;

import org.mapstruct.Named;

import java.util.UUID;

public interface BaseMapper {

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
}
