package com.scaler.controller;

import com.scaler.dto.CategoryDTO;
import com.scaler.entity.Category;
import com.scaler.entity.CategoryMapping;
import com.scaler.mapper.CategoryMapper;
import com.scaler.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories.stream()
            .map(categoryMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable UUID id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(categoryMapper.toDTO(category));
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryService.save(category);
        return ResponseEntity.ok(categoryMapper.toDTO(category));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category")
    public ResponseEntity<CategoryDTO> updateCategory(
        @PathVariable UUID id,
        @RequestBody CategoryDTO categoryDTO
    ) {
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryService.update(id, category);
        return ResponseEntity.ok(categoryMapper.toDTO(category));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get category by code")
    public ResponseEntity<CategoryDTO> getCategoryByCode(@PathVariable String code) {
        Category category = categoryService.findByCode(code);
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/mappings")
    @Operation(summary = "Create a new category mapping")
    public ResponseEntity<CategoryMapping> createCategoryMapping(@RequestParam UUID parentId, @RequestParam UUID childId) {
        Category parent = categoryService.findById(parentId);
        Category child = categoryService.findById(childId);
        CategoryMapping mapping = categoryService.createMapping(parent, child);
        return ResponseEntity.ok(mapping);
    }

    @DeleteMapping("/mappings")
    @Operation(summary = "Delete a category mapping")
    public ResponseEntity<Void> deleteCategoryMapping(@RequestParam UUID parentId, @RequestParam UUID childId) {
        Category parent = categoryService.findById(parentId);
        Category child = categoryService.findById(childId);
        List<CategoryMapping> mappings = categoryService.findMappingsByParent(parent);
        mappings.stream().filter(m -> m.getChild().equals(child)).findFirst().ifPresent(categoryService::deleteMapping);
        return ResponseEntity.noContent().build();
    }
}
