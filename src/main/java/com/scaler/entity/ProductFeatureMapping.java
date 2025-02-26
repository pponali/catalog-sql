package com.scaler.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_feature_mapping")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"product", "feature", "featureValues"})
@EqualsAndHashCode(callSuper = true, exclude = {"product", "feature", "featureValues"})
public class ProductFeatureMapping extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private ProductFeature feature;

    @OneToMany(mappedBy = "featureMapping", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeatureValue> featureValues = new HashSet<>();

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_visible")
    private Boolean visible = true;

    @Column(name = "is_enabled")
    private Boolean enabled = true;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode metadata;

    public void addFeatureValue(ProductFeatureValue value) {
        featureValues.add(value);
        value.setFeatureMapping(this);
    }

    public void removeFeatureValue(ProductFeatureValue value) {
        featureValues.remove(value);
        value.setFeatureMapping(null);
    }
}
