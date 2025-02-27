package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.entity.Channel;
import com.scaler.entity.Store;
import com.scaler.entity.enums.ChannelType;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for building channel entities.
 * Provides reusable methods to create common channel types.
 */
@Slf4j
public class ChannelBuilder {

    private ChannelBuilder() {
        // Private constructor to prevent instantiation
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates Tata CLiQ channels
     */
    public static Channel createTataCliqEcommerceChannel(Store store) throws JsonProcessingException {
        return Channel.builder()
                .code("TATACLIQ_ECOM")
                .name("Tata CLiQ E-commerce")
                .description("Main e-commerce channel for Tata CLiQ")
                .status("ACTIVE")
                .type(ChannelType.ECOMMERCE)
                .store(store)
                .enabled(true)
                .visibility(true)
                .displayOrder(1)
                .createdBy("SYSTEM")
                .channelConfig(objectMapper.writeValueAsString(createEcommerceConfig()))
                .build();
    }

    public static Channel createTataCliqMarketplaceChannel(Store store) throws JsonProcessingException {
        return Channel.builder()
                .code("TATACLIQ_MARKETPLACE")
                .name("Tata CLiQ Marketplace")
                .description("Marketplace channel for third-party sellers")
                .status("ACTIVE")
                .type(ChannelType.MARKETPLACE)
                .store(store)
                .enabled(true)
                .visibility(true)
                .displayOrder(2)
                .createdBy("SYSTEM")
                .channelConfig(objectMapper.writeValueAsString(createMarketplaceConfig()))
                .build();
    }

    /**
     * Creates Croma channels
     */
    public static Channel createCromaEcommerceChannel(Store store) throws JsonProcessingException {
        return Channel.builder()
                .code("CROMA_ECOM")
                .name("Croma E-commerce")
                .description("Online electronics store")
                .status("ACTIVE")
                .type(ChannelType.ECOMMERCE)
                .store(store)
                .enabled(true)
                .visibility(true)
                .displayOrder(1)
                .createdBy("SYSTEM")
                .channelConfig(objectMapper.writeValueAsString(createEcommerceConfig()))
                .build();
    }

    public static Channel createCromaPhysicalStoreChannel(Store store) throws JsonProcessingException {
        return Channel.builder()
                .code("CROMA_STORE")
                .name("Croma Store")
                .description("Physical electronics store")
                .status("ACTIVE")
                .type(ChannelType.PHYSICAL_STORE)
                .store(store)
                .enabled(true)
                .visibility(true)
                .displayOrder(2)
                .createdBy("SYSTEM")
                .channelConfig(objectMapper.writeValueAsString(createPhysicalStoreConfig()))
                .build();
    }

    /**
     * Creates BigBasket channels
     */
    public static Channel createBigBasketEcommerceChannel(Store store) throws JsonProcessingException {
        return Channel.builder()
                .code("BIGBASKET_ECOM")
                .name("BigBasket E-commerce")
                .description("Online grocery store")
                .status("ACTIVE")
                .type(ChannelType.ECOMMERCE)
                .store(store)
                .enabled(true)
                .visibility(true)
                .displayOrder(1)
                .createdBy("SYSTEM")
                .channelConfig(objectMapper.writeValueAsString(createEcommerceConfig()))
                .build();
    }

    public static Channel createBigBasketQuickCommerceChannel(Store store) throws JsonProcessingException {
        return Channel.builder()
                .code("BIGBASKET_QUICK")
                .name("BB Quick")
                .description("Quick commerce for groceries")
                .status("ACTIVE")
                .type(ChannelType.QUICK_COMMERCE)
                .store(store)
                .enabled(true)
                .visibility(true)
                .displayOrder(2)
                .createdBy("SYSTEM")
                .channelConfig(objectMapper.writeValueAsString(createQuickCommerceConfig()))
                .build();
    }

    /**
     * Channel configuration generators
     */
    private static Object createEcommerceConfig() {
        return new Object() {
            public final String[] paymentGateways = {"razorpay", "paytm"};
            public final String[] shippingProviders = {"delhivery", "ecom-express"};
            public final int returnPeriodDays = 30;
            public final boolean codEnabled = true;
        };
    }

    private static Object createMarketplaceConfig() {
        return new Object() {
            public final double commission = 10.0;
            public final String[] fulfillmentModes = {"seller", "warehouse"};
            public final double minimumSellerRating = 4.0;
            public final int maxDeliveryDays = 7;
        };
    }

    private static Object createPhysicalStoreConfig() {
        return new Object() {
            public final String[] posTerminals = {"T001", "T002"};
            public final String[] cashDrawers = {"CD001"};
            public final String[] receiptPrinters = {"P001"};
            public final boolean inventorySync = true;
        };
    }

    private static Object createQuickCommerceConfig() {
        return new Object() {
            public final int deliveryTimeMinutes = 30;
            public final double maxDeliveryRadius = 5.0;
            public final String[] prioritySlots = {"morning", "evening"};
            public final boolean darkStoreEnabled = true;
        };
    }
    public static Channel createCustomChannel(String code, String name, String description, ChannelType type, String status) {
        return Channel.builder()
                .code(code)
                .name(name)
                .description(description)
                .status(status)
                .type(type)
                .enabled(true)
                .visibility(true)
                .displayOrder(0)
                .build();
    }
}
