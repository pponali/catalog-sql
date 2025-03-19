package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductFeaturesRequest {
    private List<ProductFeatureDTO> features;
}
