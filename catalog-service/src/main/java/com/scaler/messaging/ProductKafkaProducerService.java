package com.scaler.messaging;

import com.scaler.catalog.proto.ProductUpdate;
import com.scaler.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductKafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(ProductKafkaProducerService.class);

    private final KafkaTemplate<String, ProductUpdate> kafkaTemplate;
    private final ProductProtoMapper productProtoMapper;
    private final String topicName;

    @Autowired
    public ProductKafkaProducerService(
            KafkaTemplate<String, ProductUpdate> kafkaTemplate,
            ProductProtoMapper productProtoMapper,
            @Value("${app.kafka.product-update-topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.productProtoMapper = productProtoMapper;
        this.topicName = topicName;
    }

    public void sendProductUpdate(Product product) {
        if (product == null || product.getId() == null) {
            log.warn("Attempted to send null product or product with null ID.");
            return;
        }

        try {
            ProductUpdate productUpdateMessage = productProtoMapper.toProductUpdateProto(product);
            if (productUpdateMessage == null) {
                 log.error("Failed to map product {} to ProductUpdate proto.", product.getId());
                 return;
            }

            String key = product.getId().toString(); // Use product ID as Kafka message key
            log.info("Sending ProductUpdate for product ID: {} to topic: {}", key, topicName);

            CompletableFuture<SendResult<String, ProductUpdate>> future = kafkaTemplate.send(topicName, key, productUpdateMessage);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Successfully sent ProductUpdate for product ID: {} to partition: {} with offset: {}",
                             key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to send ProductUpdate for product ID: {}. Error: {}", key, ex.getMessage(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Exception occurred while sending product update for ID {}: {}", product.getId(), e.getMessage(), e);
        }
    }
}
