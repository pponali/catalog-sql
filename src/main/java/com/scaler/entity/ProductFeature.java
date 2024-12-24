package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_feature")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"product", "values"})
@ToString(exclude = {"product", "values"})
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

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "feature_type")
    private String featureType;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "allowed_values")
    private String allowedValues;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private UnitOfMeasure unit;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeatureValue> values = new HashSet<>();

    private boolean required;

    public boolean isRequired() {
        return required;
    }

    public void addValue(ProductFeatureValue value) {
        values.add(value);
        value.setFeature(this);
    }

    public void removeValue(ProductFeatureValue value) {
        values.remove(value);
        value.setFeature(null);
    }

    @PrePersist
    @PreUpdate
    private void updateFromTemplate() {
        if (template != null) {
            this.code = template.getCode();
            this.name = template.getName();
            this.description = template.getDescription();
            this.featureType = template.getAttributeType();
            this.validationPattern = template.getValidationPattern();
            this.minValue = template.getMinValue();
            this.maxValue = template.getMaxValue();
            this.allowedValues = template.getAllowedValues();
            this.unit = template.getUnit();
            this.metadata = template.getMetadata();
        }
    }
}
