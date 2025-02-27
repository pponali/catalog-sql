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

}
