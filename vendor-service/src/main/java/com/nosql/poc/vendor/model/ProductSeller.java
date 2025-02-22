package com.nosql.poc.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_sellers")
public class ProductSeller {
    @Id
    private String id;

    @NotBlank
    private String productId;

    @NotBlank
    private String vendorId;

    @NotNull
    private SellerType sellerType;

    private BigDecimal sellingPrice;
    private Integer stockQuantity;
    private Boolean isActive;
    private LocalDateTime listingDate;
    private LocalDateTime lastUpdated;
    
    private String fulfillmentType; // SELF, MARKETPLACE
    private Integer processingTime; // in days
    private BigDecimal shippingCharge;
    
    // Seller-specific product attributes
    private String sellerSku;
    private String condition; // NEW, REFURBISHED, USED
    private String warrantyPeriod;
    
    // Performance metrics
    private Double sellerRating;
    private Integer totalSales;
    private Double returnRate;
    
    // Business rules
    private Integer minOrderQuantity;
    private Integer maxOrderQuantity;
    private Boolean allowPartialFulfillment;
}
