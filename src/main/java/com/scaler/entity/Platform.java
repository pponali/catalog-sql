package com.scaler.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.scaler.entity.enums.PlatformType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "platform")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"productPlatforms", "channel"})
@EqualsAndHashCode(callSuper = true, exclude = {"productPlatforms", "channel"})
public class Platform extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PlatformType type;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "min_version")
    private String minVersion;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private JsonNode metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @OneToMany(mappedBy = "platform", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private Set<ProductPlatform> productPlatforms = new HashSet<>();

    public void addProduct(Product product) {
        ProductPlatform productPlatform = ProductPlatform.builder()
                .product(product)
                .platform(this)
                .isActive(true)
                .displayOrder(productPlatforms.size() + 1)
                .status("ACTIVE")
                .build();
        productPlatforms.add(productPlatform);
    }

    public void removeProduct(Product product) {
        productPlatforms.removeIf(pp -> pp.getProduct().equals(product));
    }

    public void deactivateProduct(Product product) {
        productPlatforms.stream()
                .filter(pp -> pp.getProduct().equals(product))
                .findFirst()
                .ifPresent(pp -> {
                    pp.setIsActive(false);
                    pp.setStatus("INACTIVE");
                });
    }

    public void activateProduct(Product product) {
        productPlatforms.stream()
                .filter(pp -> pp.getProduct().equals(product))
                .findFirst()
                .ifPresent(pp -> {
                    pp.setIsActive(true);
                    pp.setStatus("ACTIVE");
                });
    }
}
