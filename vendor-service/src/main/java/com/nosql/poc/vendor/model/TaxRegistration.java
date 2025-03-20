package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxRegistration {
    private String state;
    private String stateCode;
    private String registrationNumber;
    private String registrationType;
    private Boolean active;
}