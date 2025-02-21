package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "product_price")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductPrice extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_of_business_id")
    private LineOfBusiness lineOfBusiness;
    
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    
    @Column(name = "currency", nullable = false)
    private String currency;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
