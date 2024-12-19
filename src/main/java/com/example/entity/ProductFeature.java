package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_feature")
@Data
@EqualsAndHashCode(exclude = {"product", "values"})
@ToString(exclude = {"product", "values"})
public class ProductFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private CategoryFeatureTemplate template;

    @OneToMany(
        mappedBy = "feature",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<ProductFeatureValue> values = new HashSet<>();

    public void addValue(ProductFeatureValue value) {
        values.add(value);
        value.setFeature(this);
    }

    public void removeValue(ProductFeatureValue value) {
        values.remove(value);
        value.setFeature(null);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCode() {
        return template != null ? template.getCode() : null;
    }

    public String getValueType() {
        return template != null ? template.getAttributeType() : null;
    }

    public boolean isListType() {
        return template != null && template.isMultiValued();
    }
}
