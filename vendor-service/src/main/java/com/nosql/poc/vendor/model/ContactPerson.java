package com.nosql.poc.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactPerson {
    @NotBlank
    private String name;
    
    private String designation;
    
    @Email
    private String email;
    
    private String phone;
    
    private String mobile;
    
    private String department;
    
    private Boolean isPrimary;
}