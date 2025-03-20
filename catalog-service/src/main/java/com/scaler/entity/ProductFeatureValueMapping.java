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
    @JoinColumn(name = "feature_mapping_id")
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
    
    /**
     * Sets the feature value for this mapping and ensures proper bidirectional relationship
     *
     * @param featureValue The feature value to set
     */
    public void setFeatureValue(ProductFeatureValue featureValue) {
        this.featureValue = featureValue;
        
        // Add this mapping to the feature value's product mappings if not already present
        if (featureValue != null && featureValue.getProductMappings() != null) {
            if (!featureValue.getProductMappings().contains(this)) {
                featureValue.getProductMappings().add(this);
            }
        }
    }
    
    /**
     * Sets the product for this mapping and ensures proper bidirectional relationship
     *
     * @param product The product to set
     */
    public void setProduct(Product product) {
        this.product = product;
        
        // Add this mapping to the product's feature value mappings if not already present
        if (product != null && product.getFeatureValueMappings() != null) {
            if (!product.getFeatureValueMappings().contains(this)) {
                product.getFeatureValueMappings().add(this);
            }
        }
    }
}
