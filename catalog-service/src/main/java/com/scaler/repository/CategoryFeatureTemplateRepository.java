package com.scaler.repository;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryFeatureTemplateRepository extends JpaRepository<CategoryFeatureTemplate, Long> {
    List<CategoryFeatureTemplate> findByCategory(Category category);
    Optional<CategoryFeatureTemplate> findByCategoryAndCode(Category category, String code);
}
