package com.scaler.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Locale;

@Component
public class CommonMapper {
    private static final DateTimeFormatter[] formatters = {
        DateTimeFormatter.ISO_DATE_TIME,
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a", new Locale("en", "IN"))
    };

    @Autowired
    private ObjectMapper objectMapper;

    @Named("formatDateTime")
    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? formatters[0].format(dateTime) : null;
    }

    @Named("parseDateTime")
    public LocalDateTime parseDateTime(String dateTime) {
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

    @Named("jsonStringToJsonNode")
    public JsonNode jsonStringToJsonNode(String value) {
        if (value == null || value.isEmpty() || value.equals("\"\"")) return null;
        try {
            return objectMapper.readTree(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting String to JsonNode: " + value, e);
        }
    }

    @Named("jsonNodeToString")
    public String jsonNodeToString(JsonNode jsonNode) {
        if (jsonNode == null) return null;
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JsonNode to String", e);
        }
    }

    @Named("setToList")
    public <T> List<T> setToList(Set<T> set) {
        return set != null ? set.stream().collect(Collectors.toList()) : null;
    }

    @Named("listToSet")
    public <T> Set<T> listToSet(List<T> list) {
        return list != null ? list.stream().collect(Collectors.toSet()) : null;
    }
}
