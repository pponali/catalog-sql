package com.nosql.poc.catalog.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "catalog")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"products", "merchants", "channels", "linesOfBusiness", "business", "categories", "siteCatalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"products", "merchants", "channels", "linesOfBusiness", "business", "categories", "siteCatalogs"})
public class Catalog extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private String status;
    private String type;

    private Merchant business;
    private List<Product> products = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private Set<SiteCatalog> siteCatalogs = new HashSet<>();
}
