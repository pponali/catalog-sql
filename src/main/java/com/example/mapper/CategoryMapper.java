package com.example.mapper;

import com.example.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    default Long toId(Category category) {
        return category != null ? category.getId() : null;
    }
}
