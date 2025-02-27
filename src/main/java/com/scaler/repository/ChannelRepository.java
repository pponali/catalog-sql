package com.scaler.repository;

import com.scaler.entity.Channel;
import com.scaler.entity.enums.ChannelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    List<Channel> findByStoreId(UUID storeId);
    List<Channel> findByStoreIdAndType(UUID storeId, ChannelType type);
    List<Channel> findByStoreIdAndEnabled(UUID storeId, boolean enabled);
    List<Channel> findByType(ChannelType type);
}
