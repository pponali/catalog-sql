package com.scaler.mapper;

import com.scaler.dto.CatalogDTO;
import com.scaler.entity.Catalog;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true),
        uses = {ProductMapper.class, CategoryMapper.class, SiteCatalogMapper.class})
public interface CatalogMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "businessId", source = "business.id"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    CatalogDTO toDTO(Catalog entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "business", ignore = true),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "products", ignore = true),
        @Mapping(target = "categories", ignore = true),
        @Mapping(target = "siteCatalogs", ignore = true)
    })
    Catalog toEntity(CatalogDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Catalog entity, CatalogDTO dto);

    List<CatalogDTO> toDTOList(List<Catalog> entities);
    Set<CatalogDTO> toDTOSet(Set<Catalog> entities);
}
