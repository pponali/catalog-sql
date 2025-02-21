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

@Data
public class ComplianceDocument {
    private String documentType;
    private String documentNumber;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String status;
    private String documentUrl;
}

@Data
public class Director {
    private String name;
    private String designation;
    private String din; // Director Identification Number
    private String email;
    private String phone;
    private LocalDate appointmentDate;
}
