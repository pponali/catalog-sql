package com.nosql.poc.channel.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "channel")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"products", "catalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"products", "catalogs"})
public class Channel extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private String status;
    private Set<Product> products = new HashSet<>();
    private Set<Catalog> catalogs = new HashSet<>();
}
