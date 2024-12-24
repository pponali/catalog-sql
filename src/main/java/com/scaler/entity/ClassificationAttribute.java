package com.scaler.entity;

import com.scaler.validation.rule.ValidationRule;
import jakarta.persistence.*;
import lombok.*;

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

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_class_id")
    private ClassificationClass classificationClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validation_rule_id")
    private ValidationRule validationRule;

    @OneToMany(mappedBy = "classificationAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ValidationRule> validationRules = new HashSet<>();

    @OneToMany(mappedBy = "classificationAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClassificationAttributeValue> values = new HashSet<>();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
