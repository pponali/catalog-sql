package com.scaler.builder;

import com.scaler.entity.Seller;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Utility class for building seller entities.
 * Provides reusable methods to create common seller types.
 */
@Slf4j
public class SellerBuilder {

    private SellerBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a retail seller
     * @param merchantId Associated merchant ID
     * @param code Unique seller code
     * @param name Seller name
     * @return Seller entity for retail business
     */
    public static Seller createRetailSeller(UUID merchantId, String code, String name) {
        return Seller.builder()
                .merchantId(merchantId)
                .code(code)
                .name(name)
                .type("RETAIL")
                .status("ACTIVE")
                .description("Retail business seller")
                .build();
    }

    /**
     * Creates a wholesale seller
     * @param merchantId Associated merchant ID
     * @param code Unique seller code
     * @param name Seller name
     * @return Seller entity for wholesale business
     */
    public static Seller createWholesaleSeller(UUID merchantId, String code, String name) {
        return Seller.builder()
                .merchantId(merchantId)
                .code(code)
                .name(name)
                .type("WHOLESALE")
                .status("ACTIVE")
                .description("Wholesale business seller")
                .build();
    }

    /**
     * Creates a manufacturer seller
     * @param merchantId Associated merchant ID
     * @param code Unique seller code
     * @param name Seller name
     * @return Seller entity for manufacturer
     */
    public static Seller createManufacturerSeller(UUID merchantId, String code, String name) {
        return Seller.builder()
                .merchantId(merchantId)
                .code(code)
                .name(name)
                .type("MANUFACTURER")
                .status("ACTIVE")
                .description("Direct manufacturer seller")
                .build();
    }

    /**
     * Creates a marketplace seller
     * @param merchantId Associated merchant ID
     * @param code Unique seller code
     * @param name Seller name
     * @param contactEmail Contact email
     * @param contactPhone Contact phone
     * @return Seller entity for marketplace
     */
    public static Seller createMarketplaceSeller(UUID merchantId, String code, String name, 
            String contactEmail, String contactPhone) {
        return Seller.builder()
                .merchantId(merchantId)
                .code(code)
                .name(name)
                .type("MARKETPLACE")
                .status("ACTIVE")
                .description("Marketplace integrated seller")
                .contactEmail(contactEmail)
                .contactPhone(contactPhone)
                .build();
    }

    /**
     * Creates a custom seller
     * @param merchantId Associated merchant ID
     * @param code Unique seller code
     * @param name Seller name
     * @param type Seller type
     * @param status Seller status
     * @param description Seller description
     * @param contactEmail Contact email
     * @param contactPhone Contact phone
     * @param address Physical address
     * @return Custom seller entity
     */
    public static Seller createCustomSeller(UUID merchantId, String code, String name,
            String type, String status, String description,
            String contactEmail, String contactPhone, String address) {
        return Seller.builder()
                .merchantId(merchantId)
                .code(code)
                .name(name)
                .type(type)
                .status(status)
                .description(description)
                .contactEmail(contactEmail)
                .contactPhone(contactPhone)
                .address(address)
                .build();
    }
}
