package com.scaler.mapper;

import com.scaler.dto.CategoryDTO;
import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.repository.CatalogRepository;
import com.scaler.repository.CategoryRepository;
import com.scaler.repository.MerchantRepository;
import com.scaler.util.DateUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {


    private final MerchantRepository merchantRepository;
    private final CatalogRepository catalogRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryFeatureTemplateMapper categoryFeatureTemplateMapper;

    public CategoryDTO toDTO(Category entity) {
        if (entity == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setBusinessId(entity.getMerchant() != null ? entity.getMerchant().getId() : null);
        dto.setCatalogId(entity.getCatalog() != null ? entity.getCatalog().getId() : null);
        dto.setParentId(entity.getParent() != null ? entity.getParent().getId() : null);
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedDate(DateUtil.formatDateTime(entity.getCreatedDate()));
        dto.setLastModifiedDate(DateUtil.formatDateTime(entity.getLastModifiedDate()));
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setLastModifiedBy(entity.getLastModifiedBy());

        // Map children
        if (entity.getChildren() != null) {
            dto.setChildren(entity.getChildren().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList()));
        }

        // Map templates
        if (entity.getTemplates() != null) {
            dto.setTemplates(entity.getTemplates().stream()
                    .map(categoryFeatureTemplateMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        // Map product IDs from productCategories
        if (entity.getProductCategories() != null) {
            dto.setProductIds(entity.getProductCategories().stream()
                    .map(pc -> pc.getProduct().getId())
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Category entity = Category.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .createdDate(DateUtil.parseDateTime(dto.getCreatedDate()))
                .lastModifiedDate(DateUtil.parseDateTime(dto.getLastModifiedDate()))
                .createdBy(dto.getCreatedBy())
                .lastModifiedBy(dto.getLastModifiedBy())
                .build();

        // Set merchant if businessId is provided
        if (dto.getBusinessId() != null) {
            merchantRepository.findById(dto.getBusinessId())
                    .ifPresent(entity::setMerchant);
        }

        // Set catalog if catalogId is provided
        if (dto.getCatalogId() != null) {
            catalogRepository.findById(dto.getCatalogId())
                    .ifPresent(entity::setCatalog);
        }

        // Set parent if parentId is provided
        if (dto.getParentId() != null) {
            categoryRepository.findById(dto.getParentId())
                    .ifPresent(entity::setParent);
        }

        return entity;
    }

    public void updateEntity(Category entity, CategoryDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getCode() != null) {
            entity.setCode(dto.getCode());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        // Update merchant if businessId is provided
        if (dto.getBusinessId() != null) {
            merchantRepository.findById(dto.getBusinessId())
                    .ifPresent(entity::setMerchant);
        }

        // Update catalog if catalogId is provided
        if (dto.getCatalogId() != null) {
            catalogRepository.findById(dto.getCatalogId())
                    .ifPresent(entity::setCatalog);
        }

        // Update parent if parentId is provided
        if (dto.getParentId() != null) {
            categoryRepository.findById(dto.getParentId())
                    .ifPresent(entity::setParent);
        }
    }

    public List<CategoryDTO> toDTOList(List<Category> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private Set<UUID> getProductIds(Set<Product> products) {
        if (products == null) {
            return null;
        }
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }
}
