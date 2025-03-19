package com.scaler.repository;

import com.scaler.entity.CategoryMapping;
import com.scaler.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMappingRepository extends JpaRepository<CategoryMapping, Long> {
    List<CategoryMapping> findByParent(Category parent);
    List<CategoryMapping> findByChild(Category child);
}
