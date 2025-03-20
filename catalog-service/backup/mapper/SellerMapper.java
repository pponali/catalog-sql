package com.scaler.mapper;

import com.scaler.dto.SellerDTO;
import com.scaler.entity.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, CatalogMapper.class})
public interface SellerMapper {
    
    SellerDTO toDto(Seller seller);
    
    Seller toEntity(SellerDTO dto);
    
    @Mapping(target = "id", ignore = true)
    void updateEntity(SellerDTO dto, @MappingTarget Seller seller);
}
