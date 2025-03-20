package com.scaler.builder;

import com.scaler.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Builder class for creating ProductPrice entities
 */
@Slf4j
public class ProductPriceBuilder {

    private static final Random random = new Random();

    private ProductPriceBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a product price with random value
     */
    public static ProductPrice createPrice(Product product, Merchant merchant, Channel channel, Seller seller) {
        return ProductPrice.builder()
                .product(product)
                .merchant(merchant)
                .channel(channel)
                .seller(seller)
                .price(generateRandomPrice())
                .currency("INR")
                .isActive(true)
                .createdBy("SYSTEM")
                .build();
    }

    /**
     * Creates a product price with specified value
     */
    public static ProductPrice createPrice(Product product, Merchant merchant, Channel channel, Seller seller, 
                                        BigDecimal price, String currency) {
        return ProductPrice.builder()
                .product(product)
                .merchant(merchant)
                .channel(channel)
                .seller(seller)
                .price(price)
                .currency(currency)
                .isActive(true)
                .build();
    }

    private static BigDecimal generateRandomPrice() {
        // Generate random price between 100 and 1100
        return BigDecimal.valueOf(random.nextDouble() * 1000 + 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
