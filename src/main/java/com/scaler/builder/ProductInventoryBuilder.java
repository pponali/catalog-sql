package com.scaler.builder;

import com.scaler.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Builder class for creating ProductInventory entities
 */
@Slf4j
public class ProductInventoryBuilder {

    private static final Random random = new Random();

    private ProductInventoryBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a product inventory with random quantity
     */
    public static ProductInventory createInventory(Product product, Merchant merchant, Channel channel, Seller seller) {
        return ProductInventory.builder()
                .product(product)
                .merchant(merchant)
                .channel(channel)
                .seller(seller)
                .quantity(generateRandomQuantity())
                .reservedQuantity(0)
                .createdBy("SYSTEM")
                .build();
    }

    /**
     * Creates a product inventory with specified quantity
     */
    public static ProductInventory createInventory(Product product, Merchant merchant, Channel channel, Seller seller,
                                                 Integer quantity, Integer reservedQuantity) {
        return ProductInventory.builder()
                .product(product)
                .merchant(merchant)
                .channel(channel)
                .seller(seller)
                .quantity(quantity)
                .reservedQuantity(reservedQuantity)
                .build();
    }

    private static Integer generateRandomQuantity() {
        // Generate random quantity between 50 and 150
        return random.nextInt(101) + 50;
    }
}
