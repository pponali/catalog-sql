package com.scaler.builder;

import com.scaler.entity.Platform;
import com.scaler.entity.Product;
import com.scaler.entity.ProductPlatform;

public class ProductPlatformBuilder {

    public static ProductPlatform createProductPlatform(Product product, Platform platform, Integer displayOrder, Boolean isActive) {
        return ProductPlatform.builder()
                .product(product)
                .platform(platform)
                .displayOrder(displayOrder)
                .isActive(isActive)
                .status("ACTIVE")
                .createdBy("SYSTEM")
                .build();
    }

    public static ProductPlatform createActiveProductPlatform(Product product, Platform platform) {
        return createProductPlatform(product, platform, 1, true);
    }

    public static ProductPlatform createInactiveProductPlatform(Product product, Platform platform) {
        return ProductPlatform.builder()
                .product(product)
                .platform(platform)
                .displayOrder(1)
                .isActive(false)
                .status("INACTIVE")
                .createdBy("SYSTEM")
                .build();
    }
}
