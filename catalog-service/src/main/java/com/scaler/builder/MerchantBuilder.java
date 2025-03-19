package com.scaler.builder;

import com.scaler.entity.Merchant;

public class MerchantBuilder {
    private static final String SYSTEM_USER = "system";
    
    public static Merchant createTataCliqMerchant() {
        return Merchant.builder()
                .code("TCLQ-001")
                .name("Tata CLiQ")
                .description("Tata CLiQ - Tata Group's E-commerce Platform")
                .status("ACTIVE")
                .contactEmail("support@tatacliq.com")
               // .contactPhone("+91-1800-123-1234")
              //  .address("Tata Group HQ, Mumbai")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Merchant createTata1mgMerchant() {
        return Merchant.builder()
                .code("T1MG-001")
                .name("Tata 1mg")
                .description("Tata 1mg - Healthcare Platform")
                .status("ACTIVE")
                .contactEmail("support@1mg.com")
                //.contactPhone("+91-1800-123-1235")
                //.address("Tata 1mg HQ, Gurugram")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Merchant createBigBasketMerchant() {
        return Merchant.builder()
                .code("BBKT-001")
                .name("BigBasket")
                .description("BigBasket - Online Grocery Store")
                .status("ACTIVE")
                .contactEmail("support@bigbasket.com")
                //.contactPhone("+91-1800-123-1236")
                //.address("BigBasket HQ, Bangalore")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Merchant createCromaMerchant() {
        return Merchant.builder()
                .code("CRMA-001")
                .name("Croma")
                .description("Croma - Electronics Retail Chain")
                .status("ACTIVE")
                .contactEmail("support@croma.com")
                //.contactPhone("+91-1800-123-1237")
                //.address("Croma HQ, Mumbai")
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static Merchant createTanishqMerchant() {
        return Merchant.builder()
                .code("TNSH-001")
                .name("Tanishq")
                .description("Tanishq - Premium Jewelry Brand")
                .status("ACTIVE")
                .contactEmail("support@tanishq.com")
                //.contactPhone("+91-1800-123-1238")
                //.address("Tanishq HQ, Bangalore")
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }
}
