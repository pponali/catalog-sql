package com.example.repository;

import com.example.entity.CategoryFeatureTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryFeatureTemplateRepository extends JpaRepository<CategoryFeatureTemplate, Long> {
    Set<CategoryFeatureTemplate> findByCategoryId(Long categoryId);
    Set<CategoryFeatureTemplate> findByCode(String code);
}
