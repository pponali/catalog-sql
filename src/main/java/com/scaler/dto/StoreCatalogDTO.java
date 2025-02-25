package com.scaler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class StoreCatalogDTO extends BaseDTO {

    @NotNull(message = "Site ID is required")
    private UUID siteId;

    @NotNull(message = "Catalog ID is required")
    private UUID catalogId;

    @Builder.Default
    private Boolean isDefault = false;

    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
