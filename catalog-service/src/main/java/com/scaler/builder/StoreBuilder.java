package com.scaler.builder;

import com.scaler.entity.Merchant;
import com.scaler.entity.Store;
import com.scaler.entity.StoreType;

import java.time.LocalDateTime;

public class StoreBuilder {
    
    public static Store createTataCliqStore(Merchant merchant) {
        Store store = Store.builder()
                .name("Tata CLiQ")
                .domain("tatacliq.com")
                .merchant(merchant)
                .locale("en-IN")
                .currency("INR")
                .description("Tata CLiQ Online Store")
                .active(true)
                .timezone("Asia/Kolkata")
                .status("ACTIVE")
                .storeType(StoreType.ONLINE)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        return store;
    }

    public static Store createTata1mgStore(Merchant merchant) {
        Store store = Store.builder()
                .name("Tata 1mg")
                .domain("1mg.com")
                .merchant(merchant)
                .locale("en-IN")
                .currency("INR")
                .description("Tata 1mg Healthcare Store")
                .active(true)
                .timezone("Asia/Kolkata")
                .status("ACTIVE")
                .storeType(StoreType.ONLINE)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        return store;
    }

    public static Store createBigBasketStore(Merchant merchant) {
        Store store = Store.builder()
                .name("BigBasket")
                .domain("bigbasket.com")
                .merchant(merchant)
                .locale("en-IN")
                .currency("INR")
                .description("BigBasket Online Grocery Store")
                .active(true)
                .timezone("Asia/Kolkata")
                .status("ACTIVE")
                .storeType(StoreType.ONLINE)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        return store;
    }

    public static Store createCromaStore(Merchant merchant) {
        Store store = Store.builder()
                .name("Croma")
                .domain("croma.com")
                .merchant(merchant)
                .locale("en-IN")
                .currency("INR")
                .description("Croma Electronics Store")
                .active(true)
                .timezone("Asia/Kolkata")
                .status("ACTIVE")
                .storeType(StoreType.ONLINE)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        return store;
    }

    public static Store createTanishqStore(Merchant merchant) {
        Store store = Store.builder()
                .name("Tanishq")
                .domain("tanishq.co.in")
                .merchant(merchant)
                .locale("en-IN")
                .currency("INR")
                .description("Tanishq Jewelry Store")
                .active(true)
                .timezone("Asia/Kolkata")
                .status("ACTIVE")
                .storeType(StoreType.ONLINE)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        return store;
    }
}
