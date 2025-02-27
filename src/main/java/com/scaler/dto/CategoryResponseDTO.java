package com.scaler.dto;

import com.scaler.entity.Category;
import lombok.Data;

@Data
public class CategoryResponseDTO {
    private String code;
    private String name;
    private String description;

    public static CategoryResponseDTO fromEntity(Category category) {
        if (category == null) return null;
        
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
