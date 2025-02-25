package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MerchantDTO extends BaseDTO {
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

    private List<CatalogDTO> catalogs = new ArrayList<>();

    private List<SiteDTO> sites = new ArrayList<>();
}
