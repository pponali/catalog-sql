package com.nosql.poc.channel.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MerchantDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String website;
    private String logo;
}
