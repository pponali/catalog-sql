package com.nosql.poc.vendor.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
public class TaxInformation {
    @NotBlank
    private String gstin; // GST Identification Number
    
    private String panNumber;
    
    private String taxRegistrationType;
    
    private List<String> taxExemptions;
    
    private Boolean tdsApplicable;
    
    private Double tdsPercentage;
    
    private List<TaxRegistration> stateWiseRegistrations;
    
    private Map<String, Object> additionalTaxDetails;
}

@Data
public class TaxRegistration {
    private String state;
    private String stateCode;
    private String registrationNumber;
    private String registrationType;
    private Boolean active;
}
