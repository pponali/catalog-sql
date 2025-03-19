package com.scaler.dto;

import lombok.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductFeatureWithValuesDTO extends ProductFeatureDTO {
    
    @Builder.Default
    private Set<ProductFeatureValueDTO> values = new HashSet<>();
}
