package com.scaler.mapper;

import com.scaler.dto.StoreDTO;
import com.scaler.entity.Store;
import com.scaler.entity.Merchant;
import com.scaler.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class StoreMapper {

    public Store toEntity(StoreDTO dto) {
        if (dto == null) {
            return null;
        }

        return Store.builder()
                .id(dto.getId())
                .name(dto.getName())
                .domain(dto.getDomain())
                .locale(dto.getLocale())
                .currency(dto.getCurrency())
                .description(dto.getDescription())
                .active(dto.isActive())
                .timezone(dto.getTimezone())
                .status(dto.getStatus())
                .storeType(dto.getStoreType())
                .metadata(dto.getMetadata())
                .createdDate(DateUtil.parseDateTime(dto.getCreatedDate()))
                .lastModifiedDate(DateUtil.parseDateTime(dto.getLastModifiedDate()))
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())
                .build();
    }

    public StoreDTO toDTO(Store entity) {
        if (entity == null) {
            return null;
        }

        StoreDTO dto = new StoreDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDomain(entity.getDomain());
        dto.setLocale(entity.getLocale());
        dto.setCurrency(entity.getCurrency());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        dto.setTimezone(entity.getTimezone());
        dto.setStatus(entity.getStatus());
        dto.setStoreType(entity.getStoreType());
        dto.setMetadata(entity.getMetadata());
        dto.setCreatedDate(DateUtil.formatDateTime(entity.getCreatedDate()));
        dto.setLastModifiedDate(DateUtil.formatDateTime(entity.getLastModifiedDate()));
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setLastModifiedBy(entity.getLastModifiedBy());
        
        if (entity.getMerchant() != null) {
            dto.setMerchantId(entity.getMerchant().getId());
        }

        return dto;
    }

    public Store updateEntityFromDTO(StoreDTO dto, Store entity) {
        if (dto == null || entity == null) {
            return entity;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDomain() != null) {
            entity.setDomain(dto.getDomain());
        }
        if (dto.getLocale() != null) {
            entity.setLocale(dto.getLocale());
        }
        if (dto.getCurrency() != null) {
            entity.setCurrency(dto.getCurrency());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        entity.setActive(dto.isActive());
        if (dto.getTimezone() != null) {
            entity.setTimezone(dto.getTimezone());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getStoreType() != null) {
            entity.setStoreType(dto.getStoreType());
        }
        if (dto.getMetadata() != null) {
            entity.setMetadata(dto.getMetadata());
        }

        return entity;
    }

}
