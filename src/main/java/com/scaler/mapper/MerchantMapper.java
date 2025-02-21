package com.scaler.mapper;

import com.scaler.dto.MerchantDTO;
import com.scaler.entity.Merchant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CatalogMapper.class, SiteMapper.class})
public interface MerchantMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "createdAt", source = "createdDate"),
        @Mapping(target = "lastModifiedAt", source = "lastModifiedDate"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "catalogs", source = "catalogs")
    })
    MerchantDTO toDTO(Merchant entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "createdDate", source = "createdAt"),
        @Mapping(target = "lastModifiedDate", source = "lastModifiedAt"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy"),
        @Mapping(target = "catalogs", ignore = true)
    })
    Merchant toEntity(MerchantDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Merchant entity, MerchantDTO dto);

    List<MerchantDTO> toDTOList(List<Merchant> entities);
}
