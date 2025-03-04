package com.scaler.builder;

import com.scaler.entity.Catalog;
import com.scaler.entity.Merchant;

public class CatalogBuilder {
    
    public static Catalog createElectronicsCatalog(Merchant merchant) {
        return Catalog.builder()
                .code("ELEC-001")
                .name("Electronics Catalog")
                .description("Electronics and Gadgets Catalog")
                .business(merchant)
                .status("ACTIVE")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Catalog createGroceryCatalog(Merchant merchant) {
        return Catalog.builder()
                .code("GROC-001")
                .name("Grocery Catalog")
                .description("Fresh Groceries and Daily Essentials")
                .business(merchant)
                .status("ACTIVE")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Catalog createPharmaCatalog(Merchant merchant) {
        return Catalog.builder()
                .code("PHAR-001")
                .name("Pharmacy Catalog")
                .description("Medicines and Healthcare Products")
                .business(merchant)
                .status("ACTIVE")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Catalog createFashionCatalog(Merchant merchant) {
        return Catalog.builder()
                .code("FASH-001")
                .name("Fashion Catalog")
                .description("Fashion and Lifestyle Products")
                .business(merchant)
                .status("ACTIVE")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Catalog createJewelryCatalog(Merchant merchant) {
        return Catalog.builder()
                .code("JWLRY-001")
                .name("Jewelry Catalog")
                .description("Premium Gold and Diamond Jewelry")
                .business(merchant)
                .status("ACTIVE")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
    
    public static Catalog createMobileCatalog(Merchant merchant) {
        return Catalog.builder()
                .code("MOBILE-001")
                .name("Mobile Catalog")
                .description("Mobile Devices and Accessories")
                .business(merchant)
                .status("ACTIVE")
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
}
