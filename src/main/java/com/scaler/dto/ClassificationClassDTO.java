package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationClassDTO {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String type;
    private String status;
    private UUID parentId;
    private UUID merchantId;
    private UUID catalogId;
    private List<ClassificationAttributeDTO> attributes;
    private boolean inheritFeatures;
    private Map<String, String> metadata;
}
