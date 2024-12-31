package com.scaler.entity;

import com.scaler.model.ValidationRule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "classification_attributes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ClassificationAttribute {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private String type;

    private String status;

    @lombok.Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @lombok.Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @lombok.Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_class_id")
    private ClassificationClass classificationClass;

    @Column(name = "validation_rule", columnDefinition = "jsonb")
    private String validationRule;

    @OneToMany(mappedBy = "classificationAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private Set<ClassificationAttributeValue> values = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        id = UUID.randomUUID();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
