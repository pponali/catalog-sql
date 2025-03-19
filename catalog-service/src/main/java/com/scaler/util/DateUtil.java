package com.scaler.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {


    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? formatters[0].format(dateTime) : null;
    }

    private static final DateTimeFormatter[] formatters = {
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a", new Locale("en", "IN"))
    };

    public static LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null) return null;

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateTime, formatter);
            } catch (Exception e) {
                // Try next formatter
            }
        }
        throw new IllegalArgumentException("Unable to parse date: " + dateTime);
    }
}
