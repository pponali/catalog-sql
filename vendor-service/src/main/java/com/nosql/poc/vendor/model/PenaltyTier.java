package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyTier {
    private String tierName;
    private Double tierThreshold;
    private Double penaltyRate;
    private String tierUnit;
}