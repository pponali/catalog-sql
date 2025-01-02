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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a", new Locale("en", "IN"));

    @Autowired
    private ObjectMapper objectMapper;

    @Named("formatDateTime")
    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? formatter.format(dateTime) : null;
    }

    @Named("parseDateTime")
    public LocalDateTime parseDateTime(String dateTime) {
        return dateTime != null ? LocalDateTime.parse(dateTime, formatter) : null;
    }

    @Named("mapJsonNodeToString")
    public String mapJsonNodeToString(JsonNode jsonNode) {
        try {
            return jsonNode != null ? objectMapper.writeValueAsString(jsonNode) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JsonNode to String", e);
        }
    }

    @Named("mapStringToJsonNode")
    public JsonNode mapStringToJsonNode(String json) {
        try {
            return json != null ? objectMapper.readTree(json) : null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting String to JsonNode", e);
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
