package com.nosql.poc.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {
    private String email;
    private String phone;
    private List<ContactPerson> primaryContacts;
    private List<ContactPerson> technicalContacts;
    private List<ContactPerson> billingContacts;
    private List<ContactPerson> operationalContacts;
    private Address businessAddress;
    private List<Address> shippingAddresses;
    private Map<String, Object> additionalContacts;
}
