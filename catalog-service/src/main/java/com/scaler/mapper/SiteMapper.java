package com.scaler.mapper;

import com.scaler.dto.SiteDTO;

import java.util.List;

import com.scaler.entity.Store;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MerchantMapper.class})
public interface SiteMapper {
    
    SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "merchantId", source = "merchant.id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "domain", source = "domain")
    @Mapping(target = "locale", source = "locale")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "timezone", source = "timezone")
    @Mapping(target = "status", source = "status")
    SiteDTO toDTO(Store site);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "domain", source = "domain")
    @Mapping(target = "locale", source = "locale")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "timezone", source = "timezone")
    @Mapping(target = "status", source = "status")
    Store toEntity(SiteDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "merchant", ignore = true)
    void updateEntityFromDTO(SiteDTO dto, @MappingTarget Store entity);

    List<SiteDTO> toDTOList(List<Store> entities);
}
