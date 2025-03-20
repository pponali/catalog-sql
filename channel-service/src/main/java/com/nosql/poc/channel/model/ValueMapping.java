package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a mapping between source and target values in a channel attribute mapping.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueMapping {
    private String sourceValue;
    private String targetValue;
    private String mappingLogic;
    private Double confidenceScore;
    private Boolean active;
}