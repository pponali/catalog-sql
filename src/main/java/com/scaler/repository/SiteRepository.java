package com.scaler.repository;

import com.scaler.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SiteRepository extends JpaRepository<Site, UUID> {
    List<Site> findByBusinessId(UUID businessId);
}
