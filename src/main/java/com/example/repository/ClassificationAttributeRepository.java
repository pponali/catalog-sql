package com.example.repository;

import com.example.entity.ClassificationAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificationAttributeRepository extends JpaRepository<ClassificationAttribute, Long> {
    Optional<ClassificationAttribute> findByCode(String code);
}
