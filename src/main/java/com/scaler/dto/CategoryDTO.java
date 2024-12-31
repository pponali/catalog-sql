package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private UUID id;
    private UUID businessId;
    private UUID catalogId;
    private UUID parentId;
    private String code;
    private String name;
    private String description;
    @Builder.Default
    private List<CategoryDTO> children = new ArrayList<>();
    @Builder.Default
    private List<CategoryFeatureTemplateDTO> templates = new ArrayList<>();
    @Builder.Default
    private Set<UUID> productIds = new HashSet<>();
}
