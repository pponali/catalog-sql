package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_feature_value_mapping")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"featureMapping", "featureValue", "product", "category"})
@EqualsAndHashCode(callSuper = true, exclude = {"featureMapping", "featureValue", "product", "category"})
public class ProductFeatureValueMapping extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_mapping_id", nullable = false)
    private ProductFeatureMapping featureMapping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_value_id", nullable = false)
    private ProductFeatureValue featureValue;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
