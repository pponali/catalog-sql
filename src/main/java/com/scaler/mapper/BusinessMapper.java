package com.scaler.mapper;

import com.scaler.dto.BusinessDTO;
import com.scaler.entity.Business;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CatalogMapper.class, SiteMapper.class})
public interface BusinessMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "catalogs", source = "catalogs"),
        @Mapping(target = "sites", source = "sites")
    })
    BusinessDTO toDTO(Business entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "catalogs", ignore = true),
        @Mapping(target = "sites", ignore = true)
    })
    Business toEntity(BusinessDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Business entity, BusinessDTO dto);

    List<BusinessDTO> toDTOList(List<Business> entities);
}
