package com.nosql.poc.catalog.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document(collection = "category")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "children", "templates", "products", "business", "catalog"})
@ToString(callSuper = true, exclude = {"parent", "children", "templates", "products", "business", "catalog"})
public class Category extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private Merchant merchant;
    private Catalog catalog;
    private Category parent;
    private List<Category> children = new ArrayList<>();
    private Set<CategoryFeatureTemplate> templates = new HashSet<>();
}
