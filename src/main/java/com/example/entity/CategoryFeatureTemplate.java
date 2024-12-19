package com.example.entity;

import com.example.validation.CategoryFeatureTemplateValidator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category_feature_template")
@Data
@EqualsAndHashCode(exclude = {"category", "features"})
@ToString(exclude = {"category", "features"})
@CategoryFeatureTemplateValidator
public class CategoryFeatureTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    @Column(name = "code")
    @NotBlank
    private String code;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "attribute_type")
    @NotBlank
    private String attributeType;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "min_value")
    private Double minValue;

    @Column(name = "max_value")
    private Double maxValue;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "unit")
    private String unit;

    @Column(name = "visible")
    private boolean visible = true;

    @Column(name = "editable")
    private boolean editable = true;

    @Column(name = "searchable")
    private boolean searchable = true;

    @Column(name = "comparable")
    private boolean comparable = true;

    @Column(name = "mandatory")
    private boolean mandatory = false;

    @Column(name = "multi_valued")
    private boolean multiValued = false;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private Object metadata;

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

    public CategoryFeatureTemplate createAssignment(ClassificationClass classificationClass, ClassificationAttribute attribute) {
        CategoryFeatureTemplate template = new CategoryFeatureTemplate();
        template.setCode(this.code);
        template.setName(this.name);
        template.setDescription(this.description);
        template.setAttributeType(this.attributeType);
        template.setValidationPattern(this.validationPattern);
        template.setMinValue(this.minValue);
        template.setMaxValue(this.maxValue);
        template.setAllowedValues(this.allowedValues);
        template.setUnit(this.unit);
        template.setVisible(this.visible);
        template.setEditable(this.editable);
        template.setSearchable(this.searchable);
        template.setComparable(this.comparable);
        template.setMandatory(this.mandatory);
        template.setMultiValued(this.multiValued);
        template.setMetadata(this.metadata);
        return template;
    }
}
