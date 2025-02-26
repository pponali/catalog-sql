package com.scaler.repository;

import com.scaler.entity.ProductFeatureMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductFeatureMappingRepository extends JpaRepository<ProductFeatureMapping, UUID> {
}