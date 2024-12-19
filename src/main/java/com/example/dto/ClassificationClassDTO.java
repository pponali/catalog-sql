package com.example.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationClassDTO {
    private Long id;
    private String code;
    private String name;
    private Long parentId;
    private List<ClassificationAttributeDTO> attributes;
}
