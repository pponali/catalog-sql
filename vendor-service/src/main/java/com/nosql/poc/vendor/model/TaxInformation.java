package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
