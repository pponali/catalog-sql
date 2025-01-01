package com.scaler.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "unit_of_measure")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UnitOfMeasure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "base_unit")
    private String baseUnit;

    @Column(name = "conversion_factor", nullable = false)
    private Double conversionFactor;

    @Column(nullable = false)
    private String type;

    @Column(name = "display_symbol")
    private String displaySymbol;

    @Column
    private boolean active;

    @Column(columnDefinition = "jsonb")
    private String metadata;
}
