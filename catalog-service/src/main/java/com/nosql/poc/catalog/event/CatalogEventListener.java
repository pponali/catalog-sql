package com.nosql.poc.catalog.event;

import com.nosql.poc.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogEventListener {
    
    private final CatalogService catalogService;
    
    @KafkaListener(topics = "channel-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleChannelUpdate(ChannelUpdateEvent event) {
        log.info("Received channel update event: {}", event);
        try {
            catalogService.handleChannelUpdate(event.getChannelId());
        } catch (Exception e) {
            log.error("Error handling channel update event: {}", event, e);
        }
    }
    
    @KafkaListener(topics = "vendor-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleVendorUpdate(VendorUpdateEvent event) {
        log.info("Received vendor update event: {}", event);
        try {
            catalogService.handleVendorUpdate(event.getVendorId());
        } catch (Exception e) {
            log.error("Error handling vendor update event: {}", event, e);
        }
    }
    
    @KafkaListener(topics = "product-sync-requests", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductSyncRequest(ProductSyncEvent event) {
        log.info("Received product sync request event: {}", event);
        try {
            catalogService.syncProductToChannel(event.getProductId(), event.getChannelId());
        } catch (Exception e) {
            log.error("Error handling product sync request event: {}", event, e);
        }
    }
}
