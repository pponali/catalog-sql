package com.scaler.repository;

import com.scaler.model.FeatureValueEvent;
import com.scaler.model.FeatureValueEvent.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeatureValueEventRepository extends JpaRepository<FeatureValueEvent, Long> {
    List<FeatureValueEvent> findByFeatureId(UUID featureId);
    List<FeatureValueEvent> findByEventType(EventType eventType);
    List<FeatureValueEvent> findByFeatureIdAndEventType(UUID featureId, EventType eventType);
}
