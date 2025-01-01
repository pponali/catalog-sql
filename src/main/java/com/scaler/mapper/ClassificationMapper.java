package com.scaler.mapper;

import com.scaler.dto.ClassificationAttributeDTO;
import com.scaler.dto.ClassificationClassDTO;
import com.scaler.entity.Business;
import com.scaler.entity.Catalog;
import com.scaler.entity.Category;
import com.scaler.entity.ClassificationAttribute;
import com.scaler.entity.ClassificationClass;
import org.mapstruct.*;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ClassificationMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    ClassificationAttributeDTO toDTO(ClassificationAttribute entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    ClassificationAttribute toEntity(ClassificationAttributeDTO dto);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "parentId", source = "parent.id"),
        @Mapping(target = "inheritFeatures", source = "inheritFeatures"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "businessId", source = "business.id"),
        @Mapping(target = "catalogId", source = "catalog.id")
    })
    ClassificationClassDTO toDTO(ClassificationClass entity);

    @AfterMapping
    default void setParentAndBusinessAndCatalog(ClassificationClassDTO dto, @MappingTarget ClassificationClass entity) {
        if (dto.getParentId() != null) {
            Category parent = new Category();
            parent.setId(dto.getParentId());
            entity.setParent(parent);
        }
        
        if (dto.getBusinessId() != null) {
            Business business = new Business();
            business.setId(dto.getBusinessId());
            entity.setBusiness(business);
        }
        
        if (dto.getCatalogId() != null) {
            Catalog catalog = new Catalog();
            catalog.setId(dto.getCatalogId());
            entity.setCatalog(catalog);
        }
    }

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "inheritFeatures", source = "inheritFeatures"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "parent", ignore = true),
        @Mapping(target = "business", ignore = true),
        @Mapping(target = "catalog", ignore = true),
        @Mapping(target = "attributeAssignments", ignore = true),
        @Mapping(target = "children", ignore = true),
        @Mapping(target = "templates", ignore = true),
        @Mapping(target = "products", ignore = true)
    })
    ClassificationClass toEntity(ClassificationClassDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ClassificationClass entity, ClassificationClassDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ClassificationAttribute entity, ClassificationAttributeDTO dto);
}
