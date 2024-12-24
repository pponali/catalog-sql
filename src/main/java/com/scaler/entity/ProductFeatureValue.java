package com.scaler.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_feature_value")
public class ProductFeatureValue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private ProductFeature feature;

    @Column(name = "template_id")
    private Long templateId;

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
    private JsonNode attributeValues;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public void setValidationStatus(String status) {
        this.validationStatus = status;
    }

    public void setValidationMessage(String message) {
        this.validationMessage = message;
    }
}
