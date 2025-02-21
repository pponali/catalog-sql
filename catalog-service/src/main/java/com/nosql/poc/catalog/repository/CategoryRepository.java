package com.nosql.poc.catalog.repository;

import com.scaler.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends MongoRepository<Category, UUID> {
    Optional<Category> findByCode(String code);
}
