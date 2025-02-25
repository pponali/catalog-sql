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
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"category", "features"})
@EqualsAndHashCode(callSuper = true, exclude = {"category", "features"})
@DiscriminatorValue("CATEGORY")
public class CategoryFeatureTemplate extends FeatureTemplate {
    
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "attribute_type")
    private String attributeType;

    @Column(name = "inherited")
    private boolean inherited = false;

    @Column(name = "mandatory")
    private boolean mandatory = false;

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

    public boolean isMultiValued() {
        return super.getMultiValued();
    }


    public boolean isVisible() {
        return super.getVisible();
    }


    public boolean isEditable() {
        return super.getEditable();
    }


    public boolean isSearchable() {
        return super.getSearchable();
    }


    public boolean isComparable() {
        return super.getComparable();
    }
}
