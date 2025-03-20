package com.nosql.poc.partner.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Simplified partner contract model for CSV imports
 */
@Data
@Document(collection = "partner_contracts")
public class SimplePartnerContract {
    @Id
    private String id;
    private String contractId;
    private String partnerId;
    private String contractType; // DISTRIBUTION, SALES, SERVICE, MANUFACTURING
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal contractValue;
    private String currency;
    private String paymentTerms;
    private String deliveryTerms;
    private String status; // ACTIVE, EXPIRED, TERMINATED, DRAFT
    private String terminationClause;
    private String renewalTerms;
    private Boolean autoRenewal;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter and setter methods are provided by Lombok @Data
}