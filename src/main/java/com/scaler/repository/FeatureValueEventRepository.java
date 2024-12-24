package com.scaler.repository;

import com.scaler.model.FeatureValueEvent;
import com.scaler.model.FeatureValueEvent.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureValueEventRepository extends JpaRepository<FeatureValueEvent, String> {
    List<FeatureValueEvent> findByFeatureId(String featureId);
    List<FeatureValueEvent> findByEventType(EventType eventType);
    List<FeatureValueEvent> findByFeatureIdAndEventType(String featureId, EventType eventType);
}
