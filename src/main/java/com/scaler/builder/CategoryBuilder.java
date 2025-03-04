package com.scaler.builder;

import com.scaler.entity.Catalog;
import com.scaler.entity.Category;
import com.scaler.entity.Merchant;

public class CategoryBuilder {
    
    public static Category createLaptopCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("LAPTOP")
                .name("Laptops")
                .description("All types of laptops and notebooks")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createSmartphoneCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("SMARTPHONE")
                .name("Smartphones")
                .description("Mobile phones and smartphones")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createFreshProduceCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("FRESH-PROD")
                .name("Fresh Produce")
                .description("Fresh fruits and vegetables")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createMedicinesCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("MEDICINES")
                .name("Medicines")
                .description("Prescription and over-the-counter medicines")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createGoldNecklaceCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("GOLD-NECKLACE")
                .name("Gold Necklaces")
                .description("Premium gold necklaces")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createGoldBangleCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("GOLD-BANGLE")
                .name("Gold Bangles")
                .description("Premium gold bangles")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
    
    public static Category createMobileCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("MOBILE")
                .name("Mobile Devices")
                .description("Mobile phones and accessories")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
}
