package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    private String name;
    private String designation;
    private String din; // Director Identification Number
    private String email;
    private String phone;
    private LocalDate appointmentDate;
}