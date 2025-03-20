package com.scaler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;



@Entity
@Table(name = "product_feature_value")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"productMappings", "validationResult"})
@EqualsAndHashCode(callSuper = true, exclude = {"productMappings", "validationResult"})
public class ProductFeatureValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    private ProductFeature feature;

    @OneToMany(mappedBy = "featureValue", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeatureValueMapping> productMappings = new HashSet<>();

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "type")
    private String type;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "validation_result_id")
    private ValidationResult validationResult;

    @Column(name = "attribute_values", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode attributeValues;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode metadata;

    @PrePersist
    protected void onCreate() {
        setCreatedDate(LocalDateTime.now());
        setLastModifiedDate(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        setLastModifiedDate(LocalDateTime.now());
    }

    public String getValueAsString() {
        if (attributeValues != null) {
            return attributeValues.toString();
        }
        return null;
    }

    public void setAttributeValue(JsonNode value) {
        this.attributeValues = value;
    }

    public ProductFeature getProductFeature() {
        return feature;
    }

    public String getStringValue() {
        if (attributeValues != null && attributeValues.has("stringValue")) {
            return attributeValues.get("stringValue").asText();
        }
        return null;
    }

    public Double getNumericValue() {
        if (attributeValues != null && attributeValues.has("numericValue")) {
            return attributeValues.get("numericValue").asDouble();
        }
        return null;
    }

    public Boolean getBooleanValue() {
        if (attributeValues != null && attributeValues.has("booleanValue")) {
            return attributeValues.get("booleanValue").asBoolean();
        }
        return null;
    }

    public JsonNode getAttributeValue() {
        return attributeValues;
    }
    
    /**
     * Sets the product for this feature value by creating a mapping
     * 
     * @param product The product to associate with this feature value
     */
    public void setProduct(Product product) {
        if (product == null) {
            return;
        }
        
        // Create a new mapping
        ProductFeatureValueMapping mapping = ProductFeatureValueMapping.builder()
                .product(product)
                .featureValue(this)
                .isActive(true)
                .isPrimary(false)
                .build();
                
        // Add to local collection
        if (this.productMappings == null) {
            this.productMappings = new HashSet<>();
        }
        this.productMappings.add(mapping);
        
        // Add to product's collection if it exists
        if (product.getFeatureValueMappings() != null) {
            product.getFeatureValueMappings().add(mapping);
        }
    }
}
