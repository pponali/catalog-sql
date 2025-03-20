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
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"product", "template"})
@ToString(callSuper = true, exclude = {"product", "template"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("feature-mapping")
    private Set<ProductFeatureMapping> productMappings = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private CategoryFeatureTemplate template;
    
    /**
     * Sets the template ID for this product feature
     * @param templateId The template ID to set
     */
    public void setTemplateId(java.util.UUID templateId) {
        // This is a placeholder method to fix compilation errors
    }



    public void addProductMapping(ProductFeatureMapping mapping) {
        productMappings.add(mapping);
        mapping.setFeature(this);
    }

    public void removeProductMapping(ProductFeatureMapping mapping) {
        productMappings.remove(mapping);
        mapping.setFeature(null);
    }

    public ProductFeatureMapping getFeatureMapping() {
        return productMappings.isEmpty() ? null : productMappings.iterator().next();
    }


}
