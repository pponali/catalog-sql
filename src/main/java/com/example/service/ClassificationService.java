package com.example.service;

import com.example.entity.ClassificationAttribute;
import com.example.entity.ClassificationClass;
import java.util.List;

public interface ClassificationService {
    ClassificationAttribute saveAttribute(ClassificationAttribute attribute);
    ClassificationAttribute findAttributeById(Long id);
    List<ClassificationAttribute> getAllAttributes();
    void deleteAttributeById(Long id);

    ClassificationClass saveClass(ClassificationClass classificationClass);
    ClassificationClass findClassById(Long id);
    List<ClassificationClass> getAllClasses();
    void deleteClassById(Long id);
}
