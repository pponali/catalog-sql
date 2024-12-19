package com.example.service.impl;

import com.example.entity.ClassificationAttribute;
import com.example.entity.ClassificationClass;
import com.example.repository.ClassificationAttributeRepository;
import com.example.repository.ClassificationClassRepository;
import com.example.service.ClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClassificationServiceImpl implements ClassificationService {

    private final ClassificationAttributeRepository attributeRepository;
    private final ClassificationClassRepository classRepository;

    @Override
    public ClassificationAttribute saveAttribute(ClassificationAttribute attribute) {
        return attributeRepository.save(attribute);
    }

    @Override
    public ClassificationAttribute findAttributeById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Classification attribute not found with id: " + id));
    }

    @Override
    public List<ClassificationAttribute> getAllAttributes() {
        return attributeRepository.findAll();
    }

    @Override
    public void deleteAttributeById(Long id) {
        attributeRepository.deleteById(id);
    }

    @Override
    public ClassificationClass saveClass(ClassificationClass classificationClass) {
        return classRepository.save(classificationClass);
    }

    @Override
    public ClassificationClass findClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Classification class not found with id: " + id));
    }

    @Override
    public List<ClassificationClass> getAllClasses() {
        return classRepository.findAll();
    }

    @Override
    public void deleteClassById(Long id) {
        classRepository.deleteById(id);
    }
}
