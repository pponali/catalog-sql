package com.scaler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureId implements Serializable {
    private Long productId;
    private Long classificationAttributeId;
}
