package com.scaler.repository;

import com.scaler.entity.Platform;
import com.scaler.entity.Product;
import com.scaler.entity.ProductPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductPlatformRepository extends JpaRepository<ProductPlatform, UUID> {
    
    @Query("SELECT pp FROM ProductPlatform pp WHERE pp.platform.id = :platformId AND pp.isActive = true")
    List<ProductPlatform> findActiveProductsByPlatformId(@Param("platformId") UUID platformId);

    @Query("SELECT pp FROM ProductPlatform pp WHERE pp.product.id = :productId AND pp.isActive = true")
    List<ProductPlatform> findActivePlatformsByProductId(@Param("productId") UUID productId);

    @Query("SELECT pp FROM ProductPlatform pp WHERE pp.platform.code = :platformCode AND pp.isActive = true")
    List<ProductPlatform> findActiveProductsByPlatformCode(@Param("platformCode") String platformCode);

    @Query("SELECT DISTINCT pp.product FROM ProductPlatform pp WHERE pp.platform.id = :platformId AND pp.isActive = true")
    List<Product> findProductsByPlatformId(@Param("platformId") UUID platformId);
}
