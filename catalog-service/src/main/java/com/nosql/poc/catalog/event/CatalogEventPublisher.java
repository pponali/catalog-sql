package com.nosql.poc.catalog.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogEventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishProductUpdate(String productId, Object event) {
        String topic = "product-updates";
        try {
            kafkaTemplate.send(topic, productId, event).get();
            log.info("Published product update event for product: {}", productId);
        } catch (Exception e) {
            log.error("Error publishing product update event for product: {}", productId, e);
            throw new RuntimeException("Failed to publish product update event", e);
        }
    }
    
    public void publishCategoryUpdate(String categoryId, Object event) {
        String topic = "category-updates";
        try {
            kafkaTemplate.send(topic, categoryId, event).get();
            log.info("Published category update event for category: {}", categoryId);
        } catch (Exception e) {
            log.error("Error publishing category update event for category: {}", categoryId, e);
            throw new RuntimeException("Failed to publish category update event", e);
        }
    }
    
    public void publishPriceUpdate(String productId, Object event) {
        String topic = "price-updates";
        try {
            kafkaTemplate.send(topic, productId, event).get();
            log.info("Published price update event for product: {}", productId);
        } catch (Exception e) {
            log.error("Error publishing price update event for product: {}", productId, e);
            throw new RuntimeException("Failed to publish price update event", e);
        }
    }
    
    public void publishInventoryUpdate(String productId, Object event) {
        String topic = "inventory-updates";
        try {
            kafkaTemplate.send(topic, productId, event).get();
            log.info("Published inventory update event for product: {}", productId);
        } catch (Exception e) {
            log.error("Error publishing inventory update event for product: {}", productId, e);
            throw new RuntimeException("Failed to publish inventory update event", e);
        }
    }
}
