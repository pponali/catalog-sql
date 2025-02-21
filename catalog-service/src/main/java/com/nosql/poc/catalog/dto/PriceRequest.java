package com.nosql.poc.catalog.dto;

import com.nosql.poc.catalog.model.Price;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class PriceRequest {
    @NotNull(message = "Channel ID is required")
    private String channelId;

    @NotNull(message = "Price amount is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal amount;

    private String currency;
    
    private LocalDateTime effectiveFrom;
    
    private LocalDateTime effectiveTo;
    
    private String priceType;
    
    private Map<String, Object> priceAttributes;

    public Price toPrice() {
        Price price = new Price();
        price.setChannelId(this.channelId);
        price.setAmount(this.amount);
        price.setCurrency(this.currency);
        price.setEffectiveFrom(this.effectiveFrom);
        price.setEffectiveTo(this.effectiveTo);
        price.setPriceType(this.priceType);
        price.setPriceAttributes(this.priceAttributes);
        return price;
    }
}
