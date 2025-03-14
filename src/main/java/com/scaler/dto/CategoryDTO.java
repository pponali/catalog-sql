package com.scaler.dto;

import lombok.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends BaseDTO {
    private UUID id;
    private UUID businessId;
    private UUID catalogId;
    private UUID parentId;
    private String code;
    private String type;
    private String name;
    private String description;
    @Builder.Default
    private List<CategoryDTO> children = new ArrayList<>();
    @Builder.Default
    private List<CategoryFeatureTemplateDTO> templates = new ArrayList<>();
    @Builder.Default
    private Set<UUID> productIds = new HashSet<>();
}
