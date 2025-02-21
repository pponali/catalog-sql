package com.nosql.poc.channel.event;

import com.nosql.poc.channel.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelEventListener {
    
    private final ChannelService channelService;
    
    @KafkaListener(topics = "product-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductUpdate(ProductUpdateEvent event) {
        log.info("Received product update event: {}", event);
        try {
            channelService.handleProductUpdate(event.getProductId(), event.getChannelId());
        } catch (Exception e) {
            log.error("Error handling product update event: {}", event, e);
        }
    }
    
    @KafkaListener(topics = "inventory-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInventoryUpdate(InventoryUpdateEvent event) {
        log.info("Received inventory update event: {}", event);
        try {
            channelService.handleInventoryUpdate(event.getProductId(), event.getChannelId(), event.getQuantity());
        } catch (Exception e) {
            log.error("Error handling inventory update event: {}", event, e);
        }
    }
    
    @KafkaListener(topics = "price-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handlePriceUpdate(PriceUpdateEvent event) {
        log.info("Received price update event: {}", event);
        try {
            channelService.handlePriceUpdate(event.getProductId(), event.getChannelId(), event.getPrice());
        } catch (Exception e) {
            log.error("Error handling price update event: {}", event, e);
        }
    }
    
    @KafkaListener(topics = "vendor-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleVendorUpdate(VendorUpdateEvent event) {
        log.info("Received vendor update event: {}", event);
        try {
            channelService.handleVendorUpdate(event.getVendorId(), event.getChannelId());
        } catch (Exception e) {
            log.error("Error handling vendor update event: {}", event, e);
        }
    }
}
