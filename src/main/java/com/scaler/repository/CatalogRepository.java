package com.scaler.repository;

import com.scaler.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, UUID> {
    
    List<Catalog> findByBusinessId(UUID businessId);

    @Query("SELECT DISTINCT c FROM Catalog c " +
           "JOIN c.merchants m " +
           "LEFT JOIN ChannelCatalog cc ON cc.catalog = c " +
           "WHERE m.id = :merchantId " +
           "AND (:channelId IS NULL OR cc.channel.id = :channelId)")
    Optional<Catalog> findByMerchantIdAndChannelId(
            @Param("merchantId") UUID merchantId,
            @Param("channelId") UUID channelId);
}
