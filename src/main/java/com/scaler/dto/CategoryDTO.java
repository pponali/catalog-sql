package com.scaler.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Long parentId;
}
