package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.entity.Merchant;

public class MerchantBuilder {
    private static final String SYSTEM_USER = "system";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static Merchant createTataCliqMerchant() throws JsonProcessingException {
        return Merchant.builder()
                .code("TCLQ-001")
                .name("Tata CLiQ")
                .description("Tata CLiQ - Tata Group's E-commerce Platform")
                .status("ACTIVE")
                .contactEmail("support@tatacliq.com")
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static Merchant createTataElxsiMerchant() throws JsonProcessingException {
        return Merchant.builder()
                .code("TATA_ELXSI")
                .name("Tata Elxsi")
                .description("Tata Elxsi - Design and Technology Services")
                .status("ACTIVE")
                .contactEmail("support@tataelxsi.com")
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static Merchant createTata1mgMerchant() throws JsonProcessingException {
        return Merchant.builder()
                .code("T1MG-001")
                .name("Tata 1mg")
                .description("Tata 1mg - Healthcare Platform")
                .status("ACTIVE")
                .contactEmail("support@1mg.com")

                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static Merchant createBigBasketMerchant() throws JsonProcessingException {
        return Merchant.builder()
                .code("BBKT-001")
                .name("BigBasket")
                .description("BigBasket - Online Grocery Store")
                .status("ACTIVE")
                .contactEmail("support@bigbasket.com")
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static Merchant createCromaMerchant() throws JsonProcessingException {
        return Merchant.builder()
                .code("CRMA-001")
                .name("Croma")
                .description("Croma - Electronics Retail Chain")
                .status("ACTIVE")
                .contactEmail("support@croma.com")
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static Merchant createTanishqMerchant() throws JsonProcessingException {
        return Merchant.builder()
                .code("TNSH-001")
                .name("Tanishq")
                .description("Tanishq - Premium Jewelry Brand")
                .status("ACTIVE")
                .contactEmail("support@tanishq.com")
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static Merchant createTata11Merchant() {
        return Merchant.builder()
                .code("TATA11")
                .name("Tata 11")
                .description("Tata 11 - Mobile Devices and Accessories")
                .status("ACTIVE")
                .createdBy("SYSTEM")
                .build();
    }
}
