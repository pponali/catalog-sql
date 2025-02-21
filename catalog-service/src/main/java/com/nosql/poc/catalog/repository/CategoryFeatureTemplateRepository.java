package com.nosql.poc.catalog.repository;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryFeatureTemplateRepository extends MongoRepository<CategoryFeatureTemplate, Long> {
    List<CategoryFeatureTemplate> findByCategory(Category category);
    Optional<CategoryFeatureTemplate> findByCategoryAndCode(Category category, String code);
}
