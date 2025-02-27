package com.scaler.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference("product-features")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    @JsonManagedReference("feature-mapping")
    private ProductFeature feature;

    @OneToMany(mappedBy = "featureMapping", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeatureValueMapping> featureValueMappings = new HashSet<>();

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_visible")
    private Boolean visible = true;

    @Column(name = "is_enabled")
    private Boolean enabled = true;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode metadata;

    public void addFeatureValueMapping(ProductFeatureValueMapping mapping) {
        featureValueMappings.add(mapping);
        mapping.setFeatureMapping(this);
    }

    public void removeFeatureValueMapping(ProductFeatureValueMapping mapping) {
        featureValueMappings.remove(mapping);
        mapping.setFeatureMapping(null);
    }
}
