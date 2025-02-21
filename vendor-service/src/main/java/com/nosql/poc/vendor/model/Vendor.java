package com.nosql.poc.vendor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "vendors")
public class Vendor {
    @Id
    private String id;
    
    @NotBlank
    private String vendorId;
    
    @NotBlank
    private String businessName;
    
    private LegalEntityDetails legalEntityDetails;
    
    private TaxInformation taxInformation;
    
    private ContactInformation contactInformation;
    
    private List<OperatingLocation> operatingLocations;
    
    private ServiceLevelAgreement sla;
    
    private CommissionStructure commissionStructure;
    
    private SettlementTerms settlementTerms;
    
    private List<VendorChannel> activeChannels;
    
    private QualityMetrics qualityMetrics;
    
    private Boolean active;
    
    private Map<String, Object> attributes;
    
    private AuditInfo auditInfo;
}
