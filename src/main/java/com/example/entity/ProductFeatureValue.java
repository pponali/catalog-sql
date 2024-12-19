package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_feature_values")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductFeatureValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_feature_id")
    private ProductFeature productFeature;

    @Column(name = "value")
    private String value;

    @Column(name = "unit")
    private String unit;
}
