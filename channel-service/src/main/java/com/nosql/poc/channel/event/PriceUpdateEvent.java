package com.nosql.poc.channel.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceUpdateEvent {
    private String productId;
    private String channelId;
    private BigDecimal price;
    private String currencyCode;
    private String priceType; // BASE, PROMOTIONAL, SPECIAL
    private String userId;
    private long timestamp;
}
