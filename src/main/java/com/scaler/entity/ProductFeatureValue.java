package com.scaler.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_feature_value")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"product", "feature"})
@EqualsAndHashCode(callSuper = true, exclude = {"product", "feature"})
public class ProductFeatureValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id")
    private ProductFeature feature;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "type")
    private String type;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    @Column(name = "status")
    private String status;

    @Column(name = "validation_status")
    private String validationStatus;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "validation_message")
    private String validationMessage;

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

    public String getStringValue() {
        if (attributeValues != null && attributeValues.isTextual()) {
            return attributeValues.asText();
        }
        return null;
    }

    public Double getNumericValue() {
        if (attributeValues != null && attributeValues.isNumber()) {
            return attributeValues.asDouble();
        }
        return null;
    }

    public Boolean getBooleanValue() {
        if (attributeValues != null && attributeValues.isBoolean()) {
            return attributeValues.asBoolean();
        }
        return null;
    }

    public JsonNode getAttributeValue() {
        return attributeValues;
    }

    public void setAttributeValue(JsonNode value) {
        this.attributeValues = value;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    public void setValidationStatus(String status) {
        this.validationStatus = status;
    }

    public void setValidationMessage(String message) {
        this.validationMessage = message;
    }
}
