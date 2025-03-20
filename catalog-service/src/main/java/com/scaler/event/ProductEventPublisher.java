package com.scaler.event;

import com.scaler.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${spring.kafka.topic.product-events:product-events}")
    private String productEventsTopic;
    
    public void publishProductCreated(ProductDTO product) {
        ProductEvent event = ProductEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType(ProductEvent.EventType.PRODUCT_CREATED)
                .timestamp(LocalDateTime.now())
                .productId(product.getId())
                .productData(product)
                .build();
                
        sendEvent(event);
    }
    
    public void publishProductUpdated(ProductDTO product) {
        ProductEvent event = ProductEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType(ProductEvent.EventType.PRODUCT_UPDATED)
                .timestamp(LocalDateTime.now())
                .productId(product.getId())
                .productData(product)
                .build();
                
        sendEvent(event);
    }
    
    public void publishProductDeleted(UUID productId) {
        ProductEvent event = ProductEvent.builder()
                .eventId(UUID.randomUUID())
                .eventType(ProductEvent.EventType.PRODUCT_DELETED)
                .timestamp(LocalDateTime.now())
                .productId(productId)
                .build();
                
        sendEvent(event);
    }
    
    @TransactionalEventListener
    private void sendEvent(ProductEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = 
                    kafkaTemplate.send(productEventsTopic, event.getProductId().toString(), event);
                    
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Product event sent successfully: type={}, productId={}, offset={}",
                            event.getEventType(), event.getProductId(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send product event: type={}, productId={}", 
                            event.getEventType(), event.getProductId(), ex);
                }
            });
        } catch (Exception e) {
            log.error("Error publishing product event: type={}, productId={}", 
                    event.getEventType(), event.getProductId(), e);
        }
    }
}