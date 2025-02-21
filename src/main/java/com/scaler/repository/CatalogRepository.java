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
           "JOIN c.channels ch " +
           "JOIN c.lineOfBusinesses lob " +
           "WHERE m.id = :merchantId " +
           "AND ch.id = :channelId " +
           "AND lob.id = :lineOfBusinessId")
    Optional<Catalog> findByMerchantChannelAndLob(
            @Param("merchantId") Long merchantId,
            @Param("channelId") Long channelId,
            @Param("lineOfBusinessId") Long lineOfBusinessId);
}
