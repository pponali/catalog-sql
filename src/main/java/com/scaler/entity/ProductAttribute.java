package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_attribute")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "product")
@EqualsAndHashCode(callSuper = true, exclude = "product")
public class ProductAttribute extends BaseEntity {
  
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "attribute_name")
    private String attributeName;

    @Column(name = "attribute_value")
    private String attributeValue;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;
}
