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
@ToString(callSuper = true, exclude = {"category", "template"})
@EqualsAndHashCode(callSuper = true, exclude = {"category", "template"})
public class CategoryFeatureTemplate extends BaseEntity {
    
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "feature_type", nullable = false)
    private String featureType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private FeatureTemplate template;

    @Column(name = "validation_pattern")
    private String validationPattern = "";

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "attribute_type")
    private String attributeType;

    @Column(name = "comparable")
    private Boolean comparable = false;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "searchable")
    private Boolean searchable = false;

    @Column(name = "editable")
    private Boolean editable = true;

    @Column(name = "multi_valued")
    private Boolean multiValued = false;

    @Column(name = "default_value")
    private String defaultValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private UnitOfMeasure unit;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "required")
    private Boolean required = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
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
