package com.scaler.entity;

import com.scaler.model.ValidationRule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classification_attributes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ClassificationAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

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

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
