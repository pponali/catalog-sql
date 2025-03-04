package com.scaler.builder;

import com.scaler.entity.Merchant;
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

    public static Seller createTataCliqSeller(Merchant tataCliq) {
        return Seller.builder()
                .code("TATACLIQ_DIRECT")
                .name("Nike Seller")
                .type("RETAIL")
                .status("ACTIVE")
                .merchantId(tataCliq.getId())
                .createdBy("SYSTEM")
                .description("Direct retail seller for Tata CLiQ")
                .build();
    }

    public static Seller createCromaSeller(Merchant croma) {
        return Seller.builder()
                .code("CROMA_RETAIL")
                .name("Croma Retail Seller")
                .type("RETAIL")
                .status("ACTIVE")
                .merchantId(croma.getId())
                .createdBy("SYSTEM")
                .description("Official Croma retail seller")
                .build();
    }

    public static Seller createBigBasketSeller(Merchant bigBasket) {
        return Seller.builder()
                .code("BIGBASKET_FRESH")
                .name("BigBasket Fresh Seller")
                .type("RETAIL")
                .status("ACTIVE")
                .merchantId(bigBasket.getId())
                .createdBy("SYSTEM")
                .description("BigBasket fresh produce seller")
                .build();
    }

    public static Seller createTanishqSeller(Merchant tanishq) {
        return Seller.builder()
                .code("TANISHQ_OFFICIAL")
                .name("Tanishq Official Seller")
                .type("RETAIL")
                .status("ACTIVE")
                .merchantId(tanishq.getId())
                .createdBy("SYSTEM")
                .description("Official Tanishq jewelry seller")
                .build();
    }

    public static Seller createTataElxsiSeller(Merchant tataElxsi) {
        return Seller.builder()
                .code("ELXSI_SELLER")
                .name("Tata Elxsi Design Services")
                .type("DESIGN_SERVICES")
                .status("ACTIVE")
                .merchantId(tataElxsi.getId())
                .createdBy("SYSTEM")
                .description("Official seller of Tata Elxsi design and engineering services")
                .contactEmail("support@tataelxsi.com")
                .contactPhone("+91-80-2297-9166")
                .build();
    }

    public static Seller createTata1mgSeller(Merchant tata1mg) {
        return Seller.builder()
                .code("TATA1MG_PHARMACY")
                .name("Tata 1mg Pharmacy Seller")
                .type("RETAIL")
                .status("ACTIVE")
                .merchantId(tata1mg.getId())
                .createdBy("SYSTEM")
                .description("Official Tata 1mg pharmacy seller")
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

    public static Seller createTata11Seller(Merchant merchant) {
        return Seller.builder()
                .code("TATA11_SELLER")
                .name("Tata 11 Seller")
                .description("Official seller for Tata 11 mobile products")
                .merchantId(merchant.getId())
                .type("RETAIL")
                .status("ACTIVE")
                .createdBy("SYSTEM")
                .build();
    }
}
