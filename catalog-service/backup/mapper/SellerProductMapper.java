package com.scaler.mapper;

import com.scaler.dto.SellerProductDTO;
import com.scaler.entity.SellerProduct;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class})
public interface SellerProductMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "sellerId", source = "seller.id"),
        @Mapping(target = "merchantId", source = "merchant.id"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    SellerProductDTO toDTO(SellerProduct entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "seller", ignore = true),
        @Mapping(target = "merchant", ignore = true),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "createdBy", source = "createdBy"),
        @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    })
    SellerProduct toEntity(SellerProductDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "product", ignore = true),
        @Mapping(target = "seller", ignore = true),
        @Mapping(target = "merchant", ignore = true)
    })
    void updateEntity(SellerProductDTO dto, @MappingTarget SellerProduct entity);
}
