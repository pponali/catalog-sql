package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "classification_classes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ClassificationClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ClassificationClass parent;

    @OneToMany(mappedBy = "parent")
    private Set<ClassificationClass> children = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

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

    public void addChild(ClassificationClass child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(ClassificationClass child) {
        children.remove(child);
        child.setParent(null);
    }

    public Set<ClassAttributeAssignment> getAllAttributeAssignments() {
        Set<ClassAttributeAssignment> allAssignments = new HashSet<>(attributeAssignments);
        if (inheritFeatures && parent != null) {
            allAssignments.addAll(parent.getAllAttributeAssignments());
        }
        return allAssignments;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getLevel() {
        int level = 0;
        ClassificationClass current = this;
        while (current.getParent() != null) {
            level++;
            current = current.getParent();
        }
        return level;
    }

    public List<ClassificationClass> getAncestors() {
        List<ClassificationClass> ancestors = new java.util.ArrayList<>();
        ClassificationClass current = this.getParent();
        while (current != null) {
            ancestors.add(current);
            current = current.getParent();
        }
        return ancestors;
    }

    public boolean isDescendantOf(ClassificationClass potentialAncestor) {
        ClassificationClass current = this.getParent();
        while (current != null) {
            if (current.equals(potentialAncestor)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    @PrePersist
    @PreUpdate
    private void validateHierarchy() {
        if (this.equals(parent)) {
            throw new IllegalStateException("A classification class cannot be its own parent");
        }
        if (parent != null && parent.isDescendantOf(this)) {
            throw new IllegalStateException("Circular reference detected in classification class hierarchy");
        }
    }
}
