package com.example.repository;

import com.example.entity.ClassificationAttribute;
import com.example.entity.ClassificationAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassificationAttributeValueRepository extends JpaRepository<ClassificationAttributeValue, Long> {
    Optional<ClassificationAttributeValue> findByCodeAndClassificationAttribute(String code, ClassificationAttribute attribute);
    List<ClassificationAttributeValue> findByCodeInAndClassificationAttribute(List<String> codes, ClassificationAttribute attribute);
}
