package com.scaler.repository;

import com.scaler.entity.Platform;
import com.scaler.entity.Product;
import com.scaler.entity.ProductPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductPlatformRepository extends JpaRepository<ProductPlatform, UUID> {
    
    List<ProductPlatform> findByProductId(UUID productId);
    
    List<ProductPlatform> findByPlatformId(UUID platformId);
    
    Optional<ProductPlatform> findByProductIdAndPlatformId(UUID productId, UUID platformId);
    
    List<ProductPlatform> findByPlatformIdAndIsActive(UUID platformId, boolean isActive);
    
    @Query("SELECT pp FROM ProductPlatform pp JOIN pp.platform p WHERE p.code = :platformCode")
    List<ProductPlatform> findByPlatformCode(@Param("platformCode") String platformCode);

    @Query("SELECT pp.product FROM ProductPlatform pp JOIN pp.platform p WHERE p.code = :platformCode AND pp.isActive = true")
    Collection<Product> findActiveProductsByPlatformCode(UUID platformCode);

    @Query("SELECT pp.platform FROM ProductPlatform pp JOIN pp.product p WHERE p.id = :productId AND pp.isActive = true")
    Collection<Platform> findActivePlatformsByProductId(UUID productId);
}
