package com.nosql.poc.vendor.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
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

@Data
public class PaymentInformation {
    private String paymentMode;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String accountHolderName;
    private String accountType;
    private String upiId;
    private Map<String, String> additionalDetails;
}

@Data
public class SettlementDeduction {
    private String deductionType; // TDS, COMMISSION, PENALTY
    private BigDecimal value;
    private String calculationBasis;
    private Boolean isAutoDeducted;
    private Map<String, Object> deductionRules;
}
