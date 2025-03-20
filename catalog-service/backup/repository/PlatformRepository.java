package com.scaler.repository;

import com.scaler.entity.Platform;
import com.scaler.entity.enums.PlatformType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, UUID> {
    List<Platform> findByChannelId(UUID channelId);
    List<Platform> findByChannelIdAndType(UUID channelId, PlatformType type);
    List<Platform> findByChannelIdAndIsEnabled(UUID channelId, Boolean isEnabled);
}
