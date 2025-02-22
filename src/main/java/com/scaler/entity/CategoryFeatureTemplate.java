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
@ToString(callSuper = true, exclude = {"category", "features"})
@EqualsAndHashCode(callSuper = true, exclude = {"category", "features"})
@DiscriminatorValue("CATEGORY")
public class CategoryFeatureTemplate extends FeatureTemplate {
    
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "attribute_type")
    private String attributeType;

    @Column(name = "validation_pattern")
    private String validationPattern = "";

    @Column(name = "min_value")
    private String minValue = "";

    @Column(name = "max_value")
    private String maxValue = "";

    @Column(name = "allowed_values")
    private String allowedValues = "";

    @Column(name = "default_value")
    private String defaultValue = "";

    @Column(name = "feature_type")
    private String featureType = "STRING";

    @Column(name = "data_type")
    private String dataType = "STRING";

    @Column(name = "input_type")
    private String inputType = "STRING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private UnitOfMeasure unit;

    @Column
    private boolean visible = true;

    @Column
    private boolean filterable = true;

    @Column(name = "inherited")
    private boolean inherited = false;

    @Column
    private boolean hidden = false;

    @Column
    private boolean editable = true;

    @Column
    private boolean searchable = true;

    @Column
    private boolean comparable = true;

    @Column(name = "mandatory")
    private boolean mandatory = false;

    @Column(name = "multi_valued")
    private boolean multiValued = false;

    @Column(columnDefinition = "jsonb")
    private String metadata = "{}";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public UUID getCategoryId() {
        return category != null ? category.getId() : null;
    }

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
