package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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