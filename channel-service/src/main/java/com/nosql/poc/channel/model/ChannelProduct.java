package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.nosql.poc.channel.dto.ChannelPrice;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents a product listing in a specific channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "channel_products")
public class ChannelProduct {
    /**
     * Unique identifier.
     */
    @Id
    private String id;
    
    /**
     * Reference to the product.
     */
    private String productId;
    
    /**
     * Reference to the channel.
     */
    private String channelId;
    
    /**
     * Channel-specific name.
     */
    private String name;
    
    /**
     * Channel-specific SKU.
     */
    private String sku;
    
    /**
     * Channel-specific price information.
     */
    private ChannelPrice price;
    
    /**
     * Channel-specific price amount.
     */
    private BigDecimal channelPrice;
    
    /**
     * Channel-specific currency.
     */
    private String channelCurrency;
    
    /**
     * Channel-specific description.
     */
    private String channelDescription;
    
    /**
     * Original product SKU.
     */
    private String baseProductSku;
    
    /**
     * Original product name.
     */
    private String baseProductName;
    
    /**
     * Original product description.
     */
    private String baseProductDescription;
    
    /**
     * Product categories in the channel.
     */
    private List<String> categories;
    
    /**
     * Channel-specific media urls.
     */
    private List<String> channelMedia;
    
    /**
     * Stock quantity in the channel.
     */
    private Integer stockQuantity;
    
    /**
     * Whether the product is active in the channel.
     */
    private Boolean active;
    
    /**
     * Listing status in the channel.
     */
    private String listingStatus; // PENDING, APPROVED, REJECTED, SUSPENDED
    
    /**
     * Availability status in the channel.
     */
    private String availabilityStatus; // IN_STOCK, OUT_OF_STOCK, BACK_ORDER, PREORDER
    
    /**
     * Validation errors for the channel product.
     */
    private List<String> validationErrors;
    
    /**
     * Generic attributes.
     */
    private Map<String, String> attributes;
    
    /**
     * Channel-specific attributes.
     */
    private Map<String, Object> channelAttributes;
    
    /**
     * Channel-specific configuration details.
     */
    private Map<String, Object> channelSpecificDetails;
    
    /**
     * Display priority in the channel.
     */
    private Integer displayPriority;
    
    /**
     * Fulfillment rules for this product in this channel.
     */
    private FulfillmentRules fulfillmentRules;
    
    /**
     * Creation timestamp.
     */
    private LocalDateTime createdAt;
    
    /**
     * Last update timestamp.
     */
    private LocalDateTime updatedAt;
    
    /**
     * User who created the record.
     */
    private String createdBy;
    
    /**
     * User who last updated the record.
     */
    private String updatedBy;
    
    /**
     * Initialize price from individual fields for backward compatibility.
     * 
     * @return the channel price
     */
    public ChannelPrice getPrice() {
        if (price == null && channelPrice != null) {
            price = new ChannelPrice(channelPrice, channelCurrency);
        }
        return price;
    }
    
    /**
     * Set price and update individual fields for backward compatibility.
     * 
     * @param price the channel price
     */
    public void setPrice(ChannelPrice price) {
        this.price = price;
        if (price != null) {
            this.channelPrice = price.getAmount();
            this.channelCurrency = price.getCurrency();
        }
    }
}
