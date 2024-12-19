package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "classification_classes")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClassificationClass extends Category {

    @OneToMany(mappedBy = "classificationClass")
    private Set<ClassAttributeAssignment> attributeAssignments = new HashSet<>();

    @Column(name = "allow_multiple_categories")
    private boolean allowMultipleCategories;

    @Column(name = "inherit_features")
    private boolean inheritFeatures;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "sequence")
    private Integer sequence;

    @ElementCollection
    @CollectionTable(
        name = "classification_class_metadata",
        joinColumns = @JoinColumn(name = "class_id")
    )
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> metadata = new java.util.HashMap<>();

    public Set<ClassAttributeAssignment> getAllAttributeAssignments() {
        Set<ClassAttributeAssignment> allAssignments = new HashSet<>(attributeAssignments);
        if (inheritFeatures && getParent() != null && getParent() instanceof ClassificationClass) {
            allAssignments.addAll(((ClassificationClass) getParent()).getAllAttributeAssignments());
        }
        return allAssignments;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    public boolean isLeaf() {
        return getChildren().isEmpty();
    }

    public int getLevel() {
        int level = 0;
        Category current = this;
        while (current.getParent() != null) {
            level++;
            current = current.getParent();
        }
        return level;
    }

    @Override
    public void addChild(Category child) {
        if (!(child instanceof ClassificationClass)) {
            throw new IllegalArgumentException("Can only add ClassificationClass as children");
        }
        super.addChild(child);
    }
}
