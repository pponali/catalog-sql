package com.scaler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductCategoryDTO extends BaseDTO {
    
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Merchant ID is required")
    private UUID merchantId;

    private Boolean isPrimary;

    private Integer displayOrder;

    private LocalDateTime effectiveFrom;

    private LocalDateTime effectiveTo;
}
