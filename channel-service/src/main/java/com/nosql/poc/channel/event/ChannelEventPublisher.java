package com.nosql.poc.channel.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChannelEventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publishChannelUpdate(String channelId, Object event) {
        String topic = "channel-updates";
        try {
            kafkaTemplate.send(topic, channelId, event).get();
            log.info("Published channel update event for channel: {}", channelId);
        } catch (Exception e) {
            log.error("Error publishing channel update event for channel: {}", channelId, e);
            throw new RuntimeException("Failed to publish channel update event", e);
        }
    }
    
    public void publishProductSync(String channelId, String productId, Object event) {
        String topic = "product-sync";
        try {
            kafkaTemplate.send(topic, channelId, event).get();
            log.info("Published product sync event for product: {} in channel: {}", productId, channelId);
        } catch (Exception e) {
            log.error("Error publishing product sync event for product: {} in channel: {}", productId, channelId, e);
            throw new RuntimeException("Failed to publish product sync event", e);
        }
    }
    
    public void publishInventoryUpdate(String channelId, String productId, Object event) {
        String topic = "inventory-updates";
        try {
            kafkaTemplate.send(topic, channelId, event).get();
            log.info("Published inventory update event for product: {} in channel: {}", productId, channelId);
        } catch (Exception e) {
            log.error("Error publishing inventory update event for product: {} in channel: {}", productId, channelId, e);
            throw new RuntimeException("Failed to publish inventory update event", e);
        }
    }
    
    public void publishPriceUpdate(String channelId, String productId, Object event) {
        String topic = "price-updates";
        try {
            kafkaTemplate.send(topic, channelId, event).get();
            log.info("Published price update event for product: {} in channel: {}", productId, channelId);
        } catch (Exception e) {
            log.error("Error publishing price update event for product: {} in channel: {}", productId, channelId, e);
            throw new RuntimeException("Failed to publish price update event", e);
        }
    }
}
