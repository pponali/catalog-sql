package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceDocument {
    private String documentType;
    private String documentNumber;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String status;
    private String documentUrl;
}