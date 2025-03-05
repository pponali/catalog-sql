package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category_feature_template")
@Getter
@Setter
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
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }

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
    
    public Boolean getMultiValued() {
        return super.getMultiValued();
    }
    
    public Boolean getVisible() {
        return super.getVisible();
    }
    
    public Boolean getEditable() {
        return super.getEditable();
    }
    
    public Boolean getSearchable() {
        return super.getSearchable();
    }
    
    public Boolean getComparable() {
        return super.getComparable();
    }
    
    public String getName() {
        return super.getName();
    }
    
    public String getCode() {
        return code;
    }
    
    public String getAttributeType() {
        return attributeType;
    }
    
    public String getMinValue() {
        return super.getMinValue();
    }
    
    public String getMaxValue() {
        return super.getMaxValue();
    }
    
    public String getValidationPattern() {
        return super.getValidationPattern();
    }
    
    public String getAllowedValues() {
        return super.getAllowedValues();
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
