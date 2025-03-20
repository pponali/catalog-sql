package com.scaler.productread.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.productread.document.ProductDocument;
import com.scaler.productread.service.ProductService;
import com.scaler.productread.sync.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Consumes product events from Kafka and updates the Elasticsearch index accordingly.
 * This follows the CQRS pattern where the write side (catalog-service) publishes events,
 * and the read side (product-read-service) consumes these events to update its view.
 */
@Slf4j
@Component
public class ProductEventConsumer {
    
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public ProductEventConsumer(
            ProductService productService,
            ProductMapper productMapper,
            ObjectMapper objectMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Listen for product events and process them based on event type.
     *
     * @param message The event message containing product data
     */
    @KafkaListener(topics = "${spring.kafka.topic.product-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductEvent(String message) {
        try {
            log.info("Received product event: {}", message);
            ProductEvent event = objectMapper.readValue(message, ProductEvent.class);
            
            if (event.getEventType() == null) {
                log.warn("Received product event with null event type");
                return;
            }
            
            switch (event.getEventType()) {
                case PRODUCT_CREATED:
                    handleProductCreated(event);
                    break;
                case PRODUCT_UPDATED:
                    handleProductUpdated(event);
                    break;
                case PRODUCT_DELETED:
                    handleProductDeleted(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing product event: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Process product created event and index the new product.
     *
     * @param event The product created event
     */
    private void handleProductCreated(ProductEvent event) {
        if (event.getProductData() != null) {
            ProductDocument productDocument = productMapper.mapToProductDocument(event.getProductData());
            productService.indexProduct(productDocument);
            log.info("Indexed new product: {}", event.getProductId());
        } else {
            log.warn("Product created event contained no product data for ID: {}", event.getProductId());
        }
    }
    
    /**
     * Process product updated event and update the indexed product.
     *
     * @param event The product updated event
     */
    private void handleProductUpdated(ProductEvent event) {
        if (event.getProductData() != null) {
            ProductDocument productDocument = productMapper.mapToProductDocument(event.getProductData());
            productService.indexProduct(productDocument);
            log.info("Updated indexed product: {}", event.getProductId());
        } else {
            log.warn("Product updated event contained no product data for ID: {}", event.getProductId());
        }
    }
    
    /**
     * Process product deleted event and remove the product from the index.
     *
     * @param event The product deleted event
     */
    private void handleProductDeleted(ProductEvent event) {
        UUID productId = event.getProductId();
        if (productId != null) {
            productService.deleteProduct(productId);
            log.info("Deleted product from index: {}", productId);
        } else {
            log.warn("Product deleted event contained no product ID");
        }
    }
}