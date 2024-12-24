package com.scaler.dto;

import lombok.Data;

@Data
public class UnitOfMeasureDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String baseUnit;
    private Double conversionFactor;
}
