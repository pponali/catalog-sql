package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDTO {
    private UUID id;

    @NotBlank(message = "Catalog code is required")
    @Size(min = 2, max = 50, message = "Catalog code must be between 2 and 50 characters")
    private String code;

    private String type;
    private String status;

    @NotBlank(message = "Catalog name is required")
    @Size(min = 2, max = 255, message = "Catalog name must be between 2 and 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Merchant ID is required")
    private UUID businessId;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;

    @Builder.Default
    private List<ProductDTO> products = new ArrayList<>();

    @Builder.Default
    private List<CategoryDTO> categories = new ArrayList<>();

    @Builder.Default
    private Set<SiteCatalogDTO> siteCatalogs = new HashSet<>();
}
