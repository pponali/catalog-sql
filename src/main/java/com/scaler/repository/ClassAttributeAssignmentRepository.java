package com.scaler.repository;

import com.scaler.entity.ClassAttributeAssignment;
import com.scaler.entity.ClassificationAttribute;
import com.scaler.entity.ClassificationClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ClassAttributeAssignmentRepository extends JpaRepository<ClassAttributeAssignment, Long> {
    Set<ClassAttributeAssignment> findByClassificationClass(ClassificationClass classificationClass);
    
    Set<ClassAttributeAssignment> findByClassificationClassCode(String classificationClassCode);
    
    Optional<ClassAttributeAssignment> findByClassificationClassAndClassificationAttribute(
        ClassificationClass classificationClass, 
        ClassificationAttribute classificationAttribute
    );
    
    @Query("SELECT ca FROM ClassAttributeAssignment ca " +
           "WHERE ca.classificationClass.code = :classCode " +
           "AND ca.classificationAttribute.code = :attributeCode")
    Optional<ClassAttributeAssignment> findByClassCodeAndAttributeCode(
        String classCode, 
        String attributeCode
    );
    
    boolean existsByClassificationClassAndClassificationAttribute(
        ClassificationClass classificationClass, 
        ClassificationAttribute classificationAttribute
    );
}
