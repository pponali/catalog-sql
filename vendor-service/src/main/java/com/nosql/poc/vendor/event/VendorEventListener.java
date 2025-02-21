package com.nosql.poc.vendor.event;

import com.nosql.poc.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VendorEventListener {
    
    private final VendorService vendorService;
    
    @KafkaListener(topics = "channel-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleChannelUpdate(ChannelUpdateEvent event) {
        log.info("Received channel update event: {}", event);
        try {
            vendorService.handleChannelUpdate(event.getChannelId());
        } catch (Exception e) {
            log.error("Error handling channel update event: {}", event, e);
        }
    }
    
    @KafkaListener(topics = "product-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductUpdate(ProductUpdateEvent event) {
        log.info("Received product update event: {}", event);
        try {
            vendorService.handleProductUpdate(event.getProductId(), event.getVendorId());
        } catch (Exception e) {
            log.error("Error handling product update event: {}", event, e);
        }
    }
    
    @KafkaListener(topics = "vendor-product-sync", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductSync(ProductSyncEvent event) {
        log.info("Received product sync event: {}", event);
        try {
            vendorService.syncProductToChannel(event.getProductId(), 
                event.getVendorId(), event.getChannelId());
        } catch (Exception e) {
            log.error("Error handling product sync event: {}", event, e);
        }
    }
}
