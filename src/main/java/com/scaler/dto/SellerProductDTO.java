package com.scaler.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SellerProductDTO extends BaseDTO {
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Seller ID is required")
    private UUID sellerId;

    @NotNull(message = "Merchant ID is required")
    private UUID merchantId;

    private Boolean isManufacturer;

    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;

    private String status;

    @DecimalMin(value = "0.0", message = "Commission rate must be greater than or equal to 0")
    private BigDecimal commissionRate;

}
