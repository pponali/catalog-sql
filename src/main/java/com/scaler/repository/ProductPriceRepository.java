package com.scaler.repository;

import com.scaler.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    
    /*@Query("SELECT pp FROM ProductPrice pp WHERE pp.product.id = :productId " +
           "AND (pp.channel.id = :channelId OR pp.channel IS NULL) " +
           "AND (pp.lineOfBusiness.id = :lobId OR pp.lineOfMerchant IS NULL) " +
           "AND pp.isActive = true " +
           "ORDER BY pp.channel.id NULLS LAST, pp.lineOfBusiness.id NULLS LAST")
    List<ProductPrice> findActiveProductPrices(
            @Param("productId") Long productId,
            @Param("channelId") Long channelId,
            @Param("lobId") Long lobId);*/
}
