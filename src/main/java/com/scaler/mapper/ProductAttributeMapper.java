package com.scaler.mapper;

import com.scaler.dto.ProductAttributeDTO;
import com.scaler.entity.ProductAttribute;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class})
public abstract class ProductAttributeMapper {

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "createdDate", qualifiedByName = "formatDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "formatDateTime")
    })
    public abstract ProductAttributeDTO toDTO(ProductAttribute entity);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "product.id", source = "productId"),
        @Mapping(target = "createdDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "lastModifiedDate", qualifiedByName = "parseDateTime"),
        @Mapping(target = "product", ignore = true)
    })
    public abstract ProductAttribute toEntity(ProductAttributeDTO dto);

    @Named("toDTOList")
    public abstract List<ProductAttributeDTO> toDTOList(List<ProductAttribute> entities);

    @Named("toDTOSet")
    public abstract List<ProductAttributeDTO> toDTOSet(Set<ProductAttribute> entities);

    @AfterMapping
    protected void afterToEntity(@MappingTarget ProductAttribute entity, ProductAttributeDTO dto) {
        if (entity.getProduct() != null) {
            entity.getProduct().getAttributes().add(entity);
        }
    }
}
