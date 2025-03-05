package com.scaler.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_feature")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"template", "productValueMappings"})
@ToString(callSuper = true, exclude = {"template", "productValueMappings"})
@JsonIgnoreProperties({"productValueMappings"})
public class ProductFeature extends BaseEntity {
    
    public String getValue() {
        return defaultValue;
    }
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
    private FeatureType featureType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id")
    private UnitOfMeasure unitOfMeasure;

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
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode metadata;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private CategoryFeatureTemplate template;
    
    public CategoryFeatureTemplate getTemplate() {
        return template;
    }
    
    public void setTemplate(CategoryFeatureTemplate template) {
        this.template = template;
    }
/*
    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ProductFeatureValueMapping> productValueMappings = new HashSet<>();



    public Set<ProductFeatureValue> getFeatureValues() {
        return productValueMappings.stream()
            .map(ProductFeatureValueMapping::getFeatureValue)
            .collect(java.util.stream.Collectors.toSet());
    }

    public void addProductValueMapping(ProductFeatureValueMapping mapping) {
        productValueMappings.add(mapping);
        mapping.setFeature(this);
    }

    public void removeProductValueMapping(ProductFeatureValueMapping mapping) {
        productValueMappings.remove(mapping);
        mapping.setFeature(null);
    }

    public Set<ProductFeatureValueMapping> getProductValueMappings() {
        return productValueMappings;
    }
    */



}
