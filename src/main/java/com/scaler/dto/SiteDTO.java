package com.scaler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SiteDTO extends BaseDTO {


    @NotNull(message = "Merchant ID is required")
    private UUID merchantId;

    @NotBlank(message = "Site name is required")
    @Size(min = 2, max = 255, message = "Site name must be between 2 and 255 characters")
    private String name;

    @NotBlank(message = "Domain is required")
    @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,}$", 
            message = "Invalid domain format")
    private String domain;

    @NotBlank(message = "Locale is required")
    @Pattern(regexp = "^[a-z]{2}-[A-Z]{2}$", 
            message = "Locale must be in format: xx-XX (e.g., en-US)")
    private String locale;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", 
            message = "Currency must be a 3-letter ISO code (e.g., USD)")
    private String currency;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private boolean active;
    private String timezone;
    private String status;
}
