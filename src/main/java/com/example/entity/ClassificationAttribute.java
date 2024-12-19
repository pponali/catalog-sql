package com.example.entity;

import com.example.enums.FeatureValueType;
import com.example.validation.rule.ValidationRule;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private FeatureValueType valueType;

    @Column(nullable = false)
    private boolean required;

    @Column(name = "allow_multiple")
    private boolean allowMultiple;

    @Column(name = "sequence")
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_class_id")
    private ClassificationClass classificationClass;

    @OneToMany(mappedBy = "classificationAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ValidationRule> validationRules = new HashSet<>();

    @OneToMany(mappedBy = "classificationAttribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClassificationAttributeValue> values = new HashSet<>();
}
