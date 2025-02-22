package com.scaler.repository;

import com.scaler.entity.ValidationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ValidationResultRepository extends JpaRepository<ValidationResult, String> {
    List<ValidationResult> findByEntityIdAndEntityType(UUID entityId, String entityType);
    void deleteByEntityIdAndEntityType(UUID entityId, String entityType);
}
