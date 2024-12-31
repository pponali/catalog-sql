package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@DiscriminatorValue("CLASSIFICATION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClassificationClass extends Category {

    @OneToMany(mappedBy = "classificationClass")
    @Builder.Default
    private Set<ClassAttributeAssignment> attributeAssignments = new HashSet<>();

    @Column(name = "inherit_features")
    @Builder.Default
    private boolean inheritFeatures = true;

    @ElementCollection
    @CollectionTable(name = "classification_class_metadata",
            joinColumns = @JoinColumn(name = "classification_class_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value")
    @Builder.Default
    private Map<String, String> metadata = new java.util.HashMap<>();

    public Set<ClassAttributeAssignment> getAllAttributeAssignments() {
        Set<ClassAttributeAssignment> allAssignments = new HashSet<>(attributeAssignments);
        if (inheritFeatures && getParent() != null && getParent() instanceof ClassificationClass) {
            allAssignments.addAll(((ClassificationClass) getParent()).getAllAttributeAssignments());
        }
        return allAssignments;
    }

    @PrePersist
    protected void onCreate() {
        setCreatedDate(LocalDateTime.now());
        setLastModifiedDate(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        setLastModifiedDate(LocalDateTime.now());
    }
}
