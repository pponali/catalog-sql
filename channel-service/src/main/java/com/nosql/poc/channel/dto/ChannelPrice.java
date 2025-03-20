package com.nosql.poc.channel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for channel pricing information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelPrice {
    /**
     * Main price amount.
     */
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    /**
     * List price (usually higher than sale price).
     */
    private BigDecimal listPrice;
    
    /**
     * Sale price (discounted price).
     */
    private BigDecimal salePrice;
    
    /**
     * "Compare at" price for showing savings.
     */
    private BigDecimal compareAtPrice;
    
    /**
     * When the sale starts.
     */
    private LocalDateTime saleStartDate;
    
    /**
     * When the sale ends.
     */
    private LocalDateTime saleEndDate;
    
    /**
     * Currency code (3-letter ISO).
     */
    @NotNull(message = "Currency is required")
    private String currency;
    
    /**
     * Type of price.
     */
    private String priceType; // REGULAR, SPECIAL, CLEARANCE
    
    /**
     * User who last updated the price.
     */
    private String userId;
    
    /**
     * Timestamp of the last update.
     */
    private long timestamp;
    
    /**
     * Whether tax is included in the price.
     */
    private Boolean taxIncluded;
    
    /**
     * Tax rate (as decimal).
     */
    private Double taxRate;
    
    /**
     * Constructor with just amount and currency.
     * 
     * @param amount the price amount
     * @param currency the currency code
     */
    public ChannelPrice(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
        this.priceType = "REGULAR";
        this.taxIncluded = false;
    }
}
