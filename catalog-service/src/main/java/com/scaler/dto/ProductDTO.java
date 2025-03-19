package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.databind.JsonNode;

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
public class ProductDTO extends BaseDTO {
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
    private JsonNode metadata;
    private String sku;
    private Double price;

    @NotNull(message = "Merchant ID is required")
    private UUID merchantId;

    private UUID catalogId;

    @lombok.Builder.Default
    private Set<SellerProductDTO> sellerProducts = new HashSet<>();

    private Set<UUID> categoryIds = new HashSet<>();

    @lombok.Builder.Default
    private List<ProductFeatureWithValuesDTO> features = new ArrayList<>();

    public void setFeatures(List<ProductFeatureWithValuesDTO> features) {
        this.features = features;
    }

    private List<ProductAttributeDTO> attributes = new ArrayList<>();
}
