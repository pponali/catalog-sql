package com.scaler.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "product_feature_value_mapping")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"product", "feature", "featureValue", "template"})
@EqualsAndHashCode(callSuper = true, exclude = {"product", "feature", "featureValue", "template"})
public class ProductFeatureValueMapping extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("product-features")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feature_id", nullable = false)
    private ProductFeature feature;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feature_value_id", nullable = false)
    private ProductFeatureValue featureValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id", nullable = false)
    private CategoryFeatureTemplate template;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_visible")
    private Boolean visible = true;

    @Column(name = "is_enabled")
    private Boolean enabled = true;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode metadata;
}
