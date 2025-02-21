package com.scaler.mapper;

import com.scaler.dto.SiteCatalogDTO;
import com.scaler.entity.StoreCatalog;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreCatalogMapper extends JsonNodeMapper {

    StoreCatalogMapper INSTANCE = Mappers.getMapper(StoreCatalogMapper.class);

    @Mappings({
        @Mapping(target = "siteId", expression = "java(entitySiteId(entity))"),
        @Mapping(target = "catalogId", expression = "java(entityCatalogId(entity))"),
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "isDefault", source = "isDefault"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "startDate", source = "startDate"),
        @Mapping(target = "endDate", source = "endDate"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    SiteCatalogDTO toDTO(StoreCatalog entity);

    @InheritInverseConfiguration
    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "store", ignore = true),
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
    StoreCatalog toEntity(SiteCatalogDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget StoreCatalog entity, SiteCatalogDTO dto);

    List<SiteCatalogDTO> toDTOList(List<StoreCatalog> entities);

    Set<SiteCatalogDTO> toDTOSet(Set<StoreCatalog> entities);

    default UUID entitySiteId(StoreCatalog entity) {
        return entity != null && entity.getStore() != null ? entity.getStore().getId() : null;
    }

    default UUID entityCatalogId(StoreCatalog entity) {
        return entity != null && entity.getCatalog() != null ? entity.getCatalog().getId() : null;
    }
}
