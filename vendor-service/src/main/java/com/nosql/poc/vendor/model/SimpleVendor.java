package com.nosql.poc.vendor.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Simplified vendor model for CSV imports
 */
@Data
@Document(collection = "vendors")
public class SimpleVendor {
    @Id
    private String id;
    private String vendorId;
    private String businessName;
    private SellerType sellerType;
    private String email;
    private String phone;
    private String address;
    private String legalName;
    private String taxId;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getter and setter methods are provided by Lombok @Data
}