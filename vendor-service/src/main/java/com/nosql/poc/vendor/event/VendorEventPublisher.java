package com.nosql.poc.vendor.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VendorEventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishVendorUpdate(String vendorId, Object event) {
        String topic = "vendor-updates";
        try {
            kafkaTemplate.send(topic, vendorId, event).get();
            log.info("Published vendor update event for vendor: {}", vendorId);
        } catch (Exception e) {
            log.error("Error publishing vendor update event for vendor: {}", vendorId, e);
            throw new RuntimeException("Failed to publish vendor update event", e);
        }
    }
    
    public void publishChannelAssociation(String vendorId, String channelId, Object event) {
        String topic = "vendor-channel-updates";
        try {
            kafkaTemplate.send(topic, vendorId, event).get();
            log.info("Published vendor-channel association event for vendor: {} and channel: {}", 
                    vendorId, channelId);
        } catch (Exception e) {
            log.error("Error publishing vendor-channel association event for vendor: {} and channel: {}", 
                    vendorId, channelId, e);
            throw new RuntimeException("Failed to publish vendor-channel association event", e);
        }
    }
    
    public void publishProductUpdate(String vendorId, String productId, Object event) {
        String topic = "vendor-product-updates";
        try {
            kafkaTemplate.send(topic, vendorId, event).get();
            log.info("Published vendor product update event for vendor: {} and product: {}", 
                    vendorId, productId);
        } catch (Exception e) {
            log.error("Error publishing vendor product update event for vendor: {} and product: {}", 
                    vendorId, productId, e);
            throw new RuntimeException("Failed to publish vendor product update event", e);
        }
    }
}
