package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementTerms {
    private String settlementId;
    
    private String settlementFrequency; // DAILY, WEEKLY, BIWEEKLY, MONTHLY
    
    private String settlementDay; // For weekly/monthly settlements
    
    private Integer settlementDelay; // Days after order completion
    
    private String settlementCurrency;
    
    private PaymentInformation paymentInformation;
    
    private List<SettlementDeduction> deductions;
    
    private Map<String, Object> settlementRules;
}
