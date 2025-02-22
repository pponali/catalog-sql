package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {
    private UUID id;

    @NotBlank(message = "Seller name is required")
    @Size(min = 2, max = 255, message = "Seller name must be between 2 and 255 characters")
    private String name;

    @NotBlank(message = "Seller code is required")
    @Size(min = 2, max = 50, message = "Seller code must be between 2 and 50 characters")
    private String code;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String type; // MANUFACTURER, DISTRIBUTOR, RETAILER, etc.

    private String status; // ACTIVE, INACTIVE, SUSPENDED, etc.

    private UUID merchantId; // Reference to the merchant this seller belongs to

    private String contactEmail;
    private String contactPhone;
    private String address;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;

    @Builder.Default
    private List<ProductDTO> products = new ArrayList<>();

    @Builder.Default
    private List<CatalogDTO> catalogs = new ArrayList<>();
}
