package com.scaler.mapper;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Collections;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {ProductFeatureMapper.class}, imports = {UUID.class})
public interface ProductMapper {

    @Mapping(target = "categoryIds", expression = "java(mapToCategoryIds(product))")
    @Mapping(source = "features", target = "features")
    @Mapping(target = "code", source = "code")
    ProductDTO toDTO(Product product);

    @Mapping(target = "category.id", expression = "java(mapFromCategoryIds(productDTO.getCategoryIds()))")
    @Mapping(source = "features", target = "features")
    @Mapping(target = "featureValues", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "productType", constant = "DEFAULT")
    @Mapping(target = "sku", expression = "java(UUID.randomUUID().toString())")
    Product toEntity(ProductDTO productDTO);

    List<ProductDTO> toDTO(List<Product> products);

    List<Product> toEntity(List<ProductDTO> dtos);

    default List<Long> mapToCategoryIds(Product product) {
        if (product == null || product.getCategory() == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(product.getCategory().getId());
    }

    default Long mapFromCategoryIds(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return null;
        }
        return categoryIds.get(0);
    }
}
