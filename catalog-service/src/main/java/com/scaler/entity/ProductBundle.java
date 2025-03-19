package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_bundle")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductBundle extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_product_id", nullable = false)
    private Product bundleProduct;
    
    @OneToMany(mappedBy = "bundle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BundleItem> items = new HashSet<>();
    
    @Column(name = "price_type")
    @Enumerated(EnumType.STRING)
    private BundlePriceType priceType; // FIXED or DYNAMIC
    
    @Column(name = "discount_percentage")
    private Double discountPercentage; // Optional bundle discount
}
