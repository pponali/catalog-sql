package com.example.repository;

import com.example.entity.ClassificationClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassificationClassRepository extends JpaRepository<ClassificationClass, Long> {
    Optional<ClassificationClass> findByCode(String code);
}
