package com.scaler.mapper;

import com.scaler.dto.SiteCatalogDTO;
import com.scaler.entity.SiteCatalog;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = InheritanceMappingStrategy.class)
public interface SiteCatalogMapper {

    default UUID entitySiteId(SiteCatalog entity) {
        return entity.getSite() != null ? entity.getSite().getId() : null;
    }

    default UUID entityCatalogId(SiteCatalog entity) {
        return entity.getCatalog() != null ? entity.getCatalog().getId() : null;
    }

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "siteId", expression = "java(entitySiteId(entity))"),
        @Mapping(target = "catalogId", expression = "java(entityCatalogId(entity))"),
        @Mapping(target = "isDefault", source = "isDefault"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "startDate", source = "startDate"),
        @Mapping(target = "endDate", source = "endDate"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    SiteCatalogDTO toDTO(SiteCatalog entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "site", ignore = true),
        @Mapping(target = "catalog", ignore = true),
        @Mapping(target = "isDefault", source = "isDefault"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "startDate", source = "startDate"),
        @Mapping(target = "endDate", source = "endDate"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    SiteCatalog toEntity(SiteCatalogDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget SiteCatalog entity, SiteCatalogDTO dto);

    List<SiteCatalogDTO> toDTOList(List<SiteCatalog> entities);

    Set<SiteCatalogDTO> toDTOSet(Set<SiteCatalog> entities);
}
