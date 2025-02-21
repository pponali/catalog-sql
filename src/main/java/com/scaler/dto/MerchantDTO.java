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
public class MerchantDTO {
    private UUID id;

    @NotBlank(message = "Merchant name is required")
    @Size(min = 2, max = 255, message = "Merchant name must be between 2 and 255 characters")
    private String name;

    @NotBlank(message = "Merchant code is required")
    @Size(min = 2, max = 50, message = "Merchant code must be between 2 and 50 characters")
    private String code;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String type;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String createdBy;
    private String lastModifiedBy;

    @lombok.Builder.Default
    private List<CatalogDTO> catalogs = new ArrayList<>();
    
    @lombok.Builder.Default
    private List<SiteDTO> sites = new ArrayList<>();
}
