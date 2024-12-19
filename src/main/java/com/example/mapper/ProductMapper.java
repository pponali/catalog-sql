package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ProductFeatureMapper.class})
public interface ProductMapper {
    
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "categoryIds", ignore = true)
    ProductDTO toDTO(Product product);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "features", ignore = true)
    Product toEntity(ProductDTO dto);
}
