package com.nosql.poc.partner.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Simplified partner model for CSV imports
 */
@Data
@Document(collection = "partners")
public class SimplePartner {
    @Id
    private String id;
    private String partnerId;
    private String name;
    private String description;
    private String type; // SUPPLIER, DISTRIBUTOR, RETAILER, MANUFACTURER
    private String category; // ELECTRONICS, FASHION, GROCERY, etc.
    private String status; // ACTIVE, INACTIVE, PENDING
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String taxId;
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter and setter methods are provided by Lombok @Data
}