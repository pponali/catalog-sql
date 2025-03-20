package com.nosql.poc.vendor.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class LegalEntityDetails {
    @NotBlank
    private String legalName;
    
    private String registrationType; // PRIVATE_LIMITED, PUBLIC_LIMITED, PARTNERSHIP, etc.
    
    @NotBlank
    private String registrationNumber;
    
    private LocalDate registrationDate;
    
    private String registrationAuthority;
    
    private List<String> businessLicenses;
    
    private List<ComplianceDocument> complianceDocuments;
    
    private Address registeredAddress;
    
    private List<Director> directors;
    
    private Map<String, Object> additionalDetails;
}
