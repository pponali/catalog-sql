package com.scaler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class UnitOfMeasureDTO extends BaseDTO {
    private String code;
    private String name;
    private String description;
    private String baseUnit;
    private Double conversionFactor;
}
