package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_feature")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"product", "template", "values"})
@ToString(callSuper = true, exclude = {"product", "template", "values"})
public class ProductFeature extends BaseEntity {
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "attribute_type")
    private String attributeType;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "feature_type")
    private String featureType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private UnitOfMeasure unit;

    @Column
    private boolean visible;

    @Column
    private boolean editable;

    @Column
    private boolean searchable;

    @Column
    private boolean comparable;

    @Column
    private boolean required;

    @Column(name = "multi_valued")
    private boolean multiValued;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private CategoryFeatureTemplate template;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private Set<ProductFeatureValue> values = new HashSet<>();

    public void addValue(ProductFeatureValue value) {
        values.add(value);
        value.setFeature(this);
    }

    public void removeValue(ProductFeatureValue value) {
        values.remove(value);
        value.setFeature(null);
    }
}
