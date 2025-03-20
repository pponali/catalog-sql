package com.scaler.repository;

import com.scaler.entity.FeatureTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureTemplateRepository extends JpaRepository<FeatureTemplate, UUID> {
    Optional<FeatureTemplate> findByCode(String code);
}