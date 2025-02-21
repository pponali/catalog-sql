package com.nosql.poc.vendor.model;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Data
public class ContactInformation {
    private List<ContactPerson> primaryContacts;
    private List<ContactPerson> technicalContacts;
    private List<ContactPerson> billingContacts;
    private List<ContactPerson> operationalContacts;
    private Address businessAddress;
    private List<Address> shippingAddresses;
    private Map<String, Object> additionalContacts;
}

@Data
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

@Data
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String addressType;
    private Boolean isDefault;
    private Map<String, Object> addressAttributes;
}
