package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_features")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private CategoryFeatureTemplate template;

    @OneToMany(mappedBy = "productFeature", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductFeatureValue> values = new HashSet<>();

    public void addValue(ProductFeatureValue value) {
        values.add(value);
        value.setProductFeature(this);
    }

    public void removeValue(ProductFeatureValue value) {
        values.remove(value);
        value.setProductFeature(null);
    }
}
