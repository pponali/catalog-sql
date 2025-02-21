package com.nosql.poc.copy.repository;

import com.nosql.poc.copy.model.CopyTask;
import com.nosql.poc.copy.model.CopyStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CopyTaskRepository extends MongoRepository<CopyTask, String> {
    
    List<CopyTask> findBySourceChannelId(String sourceChannelId);
    
    List<CopyTask> findByTargetChannelId(String targetChannelId);
    
    List<CopyTask> findByStatus(CopyStatus status);
    
    List<CopyTask> findByProductId(String productId);
}
