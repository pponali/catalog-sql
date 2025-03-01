package com.scaler.builder;

import com.scaler.entity.BundleItem;
import com.scaler.entity.Product;
import com.scaler.entity.ProductBundle;
import com.scaler.entity.BundlePriceType;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Builder class for creating ProductBundle entities.
 */
public class ProductBundleBuilder {

    private ProductBundleBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a product bundle with fixed pricing
     */
    public static ProductBundle createFixedPriceBundle(Product bundleProduct, Set<BundleItem> items, Double discountPercentage) {
        return ProductBundle.builder()
                .bundleProduct(bundleProduct)
                .items(items)
                .priceType(BundlePriceType.FIXED)
                .discountPercentage(discountPercentage)
                .createdBy("SYSTEM")
                .lastModifiedBy("SYSTEM")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a product bundle with dynamic pricing
     */
    public static ProductBundle createDynamicPriceBundle(Product bundleProduct, Set<BundleItem> items) {
        return ProductBundle.builder()
                .bundleProduct(bundleProduct)
                .items(items)
                .priceType(BundlePriceType.DYNAMIC)
                .createdBy("SYSTEM")
                .lastModifiedBy("SYSTEM")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a bundle item
     */
    public static BundleItem createBundleItem(ProductBundle bundle, Product product, Integer quantity) {
        return BundleItem.builder()
                .bundle(bundle)
                .product(product)
                .quantity(quantity)
                .createdBy("SYSTEM")
                .lastModifiedBy("SYSTEM")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }
}
