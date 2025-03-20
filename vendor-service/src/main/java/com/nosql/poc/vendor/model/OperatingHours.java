package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatingHours {
    private String dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isHoliday;
    private String shiftType;
}