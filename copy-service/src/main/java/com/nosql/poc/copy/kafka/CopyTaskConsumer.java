package com.nosql.poc.copy.kafka;

import com.nosql.poc.copy.model.CopyTask;
import com.nosql.poc.copy.service.CopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CopyTaskConsumer {
    
    private final CopyService copyService;
    
    @KafkaListener(topics = "product-copy-tasks", groupId = "copy-service")
    public void consumeCopyTask(CopyTask task) {
        copyService.processCopyTask(task);
    }
}
