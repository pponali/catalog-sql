package com.scaler.repository;

import com.scaler.model.FeatureValueAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeatureValueAuditRepository extends JpaRepository<FeatureValueAudit, Long> {
    
    Page<FeatureValueAudit> findByValueId(Long valueId, Pageable pageable);
    
    Page<FeatureValueAudit> findByFeatureId(Long featureId, Pageable pageable);
    
    Page<FeatureValueAudit> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    Page<FeatureValueAudit> findByAction(FeatureValueAudit.AuditAction action, Pageable pageable);
    
    List<FeatureValueAudit> findByFeatureId(Long featureId);
    
    Page<FeatureValueAudit> findByFeatureIdAndAction(Long featureId, FeatureValueAudit.AuditAction action, Pageable pageable);
    
    List<FeatureValueAudit> findTop10ByOrderByTimestampDesc();
    
    List<FeatureValueAudit> findByFeatureValueIdOrderByTimestampDesc(Long featureValueId);
    
    List<FeatureValueAudit> findByFeatureIdOrderByTimestampDesc(Long featureId);
    
    Page<FeatureValueAudit> findByFeatureIdAndTimestampBetween(
            Long featureId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    @Query("SELECT a FROM FeatureValueAudit a WHERE a.featureId = :featureId " +
           "AND a.timestamp >= :start AND a.timestamp < :end " +
           "AND a.action IN :actions")
    List<FeatureValueAudit> findByFeatureIdAndTimeRangeAndActions(
            @Param("featureId") Long featureId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("actions") List<FeatureValueAudit.AuditAction> actions);
    
    @Query("SELECT COUNT(a) FROM FeatureValueAudit a WHERE a.featureId = :featureId " +
           "AND a.timestamp >= :start AND a.timestamp < :end " +
           "GROUP BY FUNCTION('date_trunc', :interval, a.timestamp)")
    List<Long> getChangeFrequency(
            @Param("featureId") Long featureId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("interval") String interval);
}
