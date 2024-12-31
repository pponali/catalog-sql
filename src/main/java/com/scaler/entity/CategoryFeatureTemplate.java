package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category_feature_template")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "category")
@EqualsAndHashCode(callSuper = true, exclude = "category")
public class CategoryFeatureTemplate extends BaseEntity {
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String attributeType;

    @Column(name = "validation_pattern")
    @lombok.Builder.Default
    private String validationPattern = "";

    @Column(name = "min_value")
    @lombok.Builder.Default
    private String minValue = "";

    @Column(name = "max_value")
    @lombok.Builder.Default
    private String maxValue = "";

    @Column(name = "allowed_values")
    @lombok.Builder.Default
    private String allowedValues = "";

    @Column(name = "default_value")
    @lombok.Builder.Default
    private String defaultValue = "";

    @Column(name = "feature_type")
    @lombok.Builder.Default
    private String featureType = "STRING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private UnitOfMeasure unit;

    @Column
    @lombok.Builder.Default
    private boolean visible = true;

    @Column
    @lombok.Builder.Default
    private boolean editable = true;

    @Column
    @lombok.Builder.Default
    private boolean searchable = true;

    @Column
    @lombok.Builder.Default
    private boolean comparable = true;

    @Column
    @lombok.Builder.Default
    private boolean mandatory = false;

    @Column(name = "multi_valued")
    @lombok.Builder.Default
    private boolean multiValued = false;

    @Column(columnDefinition = "jsonb")
    @lombok.Builder.Default
    private String metadata = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private Set<ProductFeature> features = new HashSet<>();

    public void addFeature(ProductFeature feature) {
        features.add(feature);
        feature.setTemplate(this);
    }

    public void removeFeature(ProductFeature feature) {
        features.remove(feature);
        feature.setTemplate(null);
    }
}
