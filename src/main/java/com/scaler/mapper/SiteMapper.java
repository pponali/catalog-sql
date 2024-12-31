package com.scaler.mapper;

import com.scaler.dto.SiteDTO;
import com.scaler.entity.Site;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SiteMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "businessId", source = "business.id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "domain", source = "domain"),
        @Mapping(target = "locale", source = "locale"),
        @Mapping(target = "currency", source = "currency"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "active", source = "active"),
        @Mapping(target = "timezone", source = "timezone"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    SiteDTO toDTO(Site entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "business", ignore = true),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "domain", source = "domain"),
        @Mapping(target = "locale", source = "locale"),
        @Mapping(target = "currency", source = "currency"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "active", source = "active"),
        @Mapping(target = "timezone", source = "timezone"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    Site toEntity(SiteDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Site entity, SiteDTO dto);

    List<SiteDTO> toDTOList(List<Site> entities);
}
