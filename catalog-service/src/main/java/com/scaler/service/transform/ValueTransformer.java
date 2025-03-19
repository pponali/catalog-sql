package com.scaler.service.transform;

import com.scaler.dto.ProductFeatureValueDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ValueTransformer {

    private final ObjectMapper objectMapper;
    private final Map<String, Function<JsonNode, JsonNode>> transformers = new HashMap<>();

    {
        transformers.put("normalize", this::normalizeNumeric);
        transformers.put("standardize", this::standardizeString);
        transformers.put("round", this::roundNumeric);
        transformers.put("uppercase", this::toUpperCase);
        transformers.put("lowercase", this::toLowerCase);
        transformers.put("trim", this::trimString);
    }

    public ProductFeatureValueDTO transform(ProductFeatureValueDTO dto, List<String> transformations) {
        JsonNode value = dto.getAttributeValue();
        
        for (String transformation : transformations) {
            Function<JsonNode, JsonNode> transformer = transformers.get(transformation);
            if (transformer != null) {
                value = transformer.apply(value);
            }
        }
        
        dto.setAttributeValue(value);
        return dto;
    }

    private JsonNode normalizeNumeric(JsonNode node) {
        if (!"number".equals(node.get("type").asText())) {
            return node;
        }

        double value = node.get("value").asDouble();
        double normalized = (value - getMin()) / (getMax() - getMin());

        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "number");
        result.put("value", normalized);
        return result;
    }

    private JsonNode standardizeString(JsonNode node) {
        if (!"string".equals(node.get("type").asText())) {
            return node;
        }

        String value = node.get("value").asText();
        String standardized = value.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "string");
        result.put("value", standardized);
        return result;
    }

    private JsonNode roundNumeric(JsonNode node) {
        if (!"number".equals(node.get("type").asText())) {
            return node;
        }

        double value = node.get("value").asDouble();
        long rounded = Math.round(value);

        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "number");
        result.put("value", rounded);
        return result;
    }

    private JsonNode toUpperCase(JsonNode node) {
        if (!"string".equals(node.get("type").asText())) {
            return node;
        }

        String value = node.get("value").asText();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "string");
        result.put("value", value.toUpperCase());
        return result;
    }

    private JsonNode toLowerCase(JsonNode node) {
        if (!"string".equals(node.get("type").asText())) {
            return node;
        }

        String value = node.get("value").asText();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "string");
        result.put("value", value.toLowerCase());
        return result;
    }

    private JsonNode trimString(JsonNode node) {
        if (!"string".equals(node.get("type").asText())) {
            return node;
        }

        String value = node.get("value").asText();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "string");
        result.put("value", value.trim());
        return result;
    }

    // Placeholder methods for min/max values
    private double getMin() {
        return 0.0;
    }

    private double getMax() {
        return 100.0;
    }
}
