package com.nosql.poc.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Simplified product seller model for CSV imports
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_sellers")
public class SimpleProductSeller {
    @Id
    private String id;
    private String productId;
    private String vendorId;
    private SellerType sellerType;
    private BigDecimal sellingPrice;
    private Integer stockQuantity;
    private Boolean isActive;
    private LocalDateTime listingDate;
    private LocalDateTime lastUpdated;
    private String fulfillmentType; // SELF, MARKETPLACE
    private Integer processingTime; // in days
    private BigDecimal shippingCharge;
    private String sellerSku;
    private String condition; // NEW, REFURBISHED, USED
    private String warrantyPeriod;
    private Double sellerRating;
    private Integer totalSales;
    private Double returnRate;
    private Integer minOrderQuantity;
    private Integer maxOrderQuantity;
    private Boolean allowPartialFulfillment;
}