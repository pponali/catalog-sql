package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;

    @NotBlank(message = "Product code is required")
    @Size(min = 2, max = 50, message = "Product code must be between 2 and 50 characters")
    private String code;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Product type is required")
    private String productType;

    private String status;
    private String metadata;
    private String sku;
    private Double price;

    @NotNull(message = "Business ID is required")
    private UUID businessId;

    private UUID catalogId;
    private UUID unitOfMeasureId;

    private String createdDate;
    private String lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    private Set<UUID> categoryIds = new HashSet<>();

    private List<ProductFeatureDTO> features = new ArrayList<>();
    private List<ProductAttributeDTO> attributes = new ArrayList<>();
}
