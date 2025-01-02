package com.scaler.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface JsonNodeMapper {
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default JsonNode map(String value) {
        try {
            return value != null ? OBJECT_MAPPER.readTree(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    default String map(JsonNode value) {
        return value != null ? value.toString() : null;
    }
}
