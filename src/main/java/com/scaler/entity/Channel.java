package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "channel")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"products", "catalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"products", "catalogs"})
public class Channel extends BaseEntity {
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;
    
    @ManyToMany(mappedBy = "channels")
    private Set<Product> products = new HashSet<>();
    
    @ManyToMany(mappedBy = "channels")
    private Set<Catalog> catalogs = new HashSet<>();
}
