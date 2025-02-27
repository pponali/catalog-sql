package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.entity.Channel;
import com.scaler.entity.Product;
import com.scaler.entity.ProductChannel;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ProductChannelBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ProductChannelBuilder() {
        // Private constructor to prevent instantiation
    }

    public static ProductChannel createEcommerceProductChannel(Product product, Channel channel) throws JsonProcessingException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("price", Map.of(
            "regular", 89999.00,
            "sale", 79999.00,
            "saleStartDate", LocalDateTime.now().plusDays(1).toString(),
            "saleEndDate", LocalDateTime.now().plusDays(15).toString()
        ));
        metadata.put("inventory", Map.of(
            "availableQuantity", 100,
            "minOrderQuantity", 1,
            "maxOrderQuantity", 2
        ));
        metadata.put("shipping", Map.of(
            "freeShipping", true,
            "estimatedDeliveryDays", 3
        ));

        return ProductChannel.builder()
                .product(product)
                .channel(channel)
                .isEnabled(true)
                .isVisible(true)
                .createdBy("SYSTEM")
                .effectiveFrom(LocalDateTime.now())
                .channelMetadata(objectMapper.writeValueAsString(metadata))
                .build();
    }

    public static ProductChannel createMarketplaceProductChannel(Product product, Channel channel) throws JsonProcessingException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("price", Map.of(
            "regular", 89999.00,
            "commission", 10.0,
            "minimumPrice", 79999.00
        ));
        metadata.put("seller", Map.of(
            "fulfillment", "seller",
            "returnPeriod", 7,
            "warranty", "1 year"
        ));

        return ProductChannel.builder()
                .product(product)
                .channel(channel)
                .isEnabled(true)
                .isVisible(true)
                .createdBy("SYSTEM")
                .effectiveFrom(LocalDateTime.now())
                .channelMetadata(objectMapper.writeValueAsString(metadata))
                .build();
    }

    public static ProductChannel createQuickCommerceProductChannel(Product product, Channel channel) throws JsonProcessingException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("delivery", Map.of(
            "maxQuantity", 5,
            "timeSlots", new String[]{"morning", "evening"},
            "deliveryFee", 40.0,
            "minOrderValue", 200.0
        ));
        metadata.put("inventory", Map.of(
            "darkStoreId", "DS001",
            "availableQuantity", 50,
            "reorderPoint", 20
        ));

        return ProductChannel.builder()
                .product(product)
                .channel(channel)
                .isEnabled(true)
                .isVisible(true)
                .createdBy("SYSTEM")
                .effectiveFrom(LocalDateTime.now())
                .effectiveTo(LocalDateTime.now().plusHours(12))
                .channelMetadata(objectMapper.writeValueAsString(metadata))
                .build();
    }

    public static ProductChannel createPhysicalStoreProductChannel(Product product, Channel channel) throws JsonProcessingException {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("store", Map.of(
            "displayLocation", "Main Floor",
            "stockLocation", "Warehouse A",
            "minDisplayQuantity", 2
        ));
        metadata.put("pricing", Map.of(
            "mrp", 89999.00,
            "storeDiscount", 5.0,
            "memberDiscount", 2.0
        ));

        return ProductChannel.builder()
                .product(product)
                .channel(channel)
                .isEnabled(true)
                .isVisible(true)
                .createdBy("SYSTEM")
                .effectiveFrom(LocalDateTime.now())
                .channelMetadata(objectMapper.writeValueAsString(metadata))
                .build();
    }
}
