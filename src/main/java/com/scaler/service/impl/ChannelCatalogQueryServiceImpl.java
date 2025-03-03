package com.scaler.service.impl;

import com.scaler.entity.Catalog;
import com.scaler.entity.Channel;
import com.scaler.entity.ChannelCatalog;
import com.scaler.repository.ChannelCatalogRepository;
import com.scaler.service.ChannelCatalogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelCatalogQueryServiceImpl implements ChannelCatalogQueryService {

    private final ChannelCatalogRepository channelCatalogRepository;

    @Override
    public List<ChannelCatalog> getAllChannelCatalogs() {
        return channelCatalogRepository.findAll();
    }

    @Override
    public Optional<ChannelCatalog> getChannelCatalogById(UUID id) {
        return channelCatalogRepository.findById(id);
    }

    @Override
    public List<ChannelCatalog> getChannelCatalogsByChannelId(UUID channelId) {
        return channelCatalogRepository.findByChannelId(channelId);
    }

    @Override
    public List<ChannelCatalog> getChannelCatalogsByCatalogId(UUID catalogId) {
        return channelCatalogRepository.findByCatalogId(catalogId);
    }

    @Override
    public Optional<ChannelCatalog> getChannelCatalogByChannelAndCatalog(UUID channelId, UUID catalogId) {
        return channelCatalogRepository.findByChannelIdAndCatalogId(channelId, catalogId);
    }

    @Override
    public List<Catalog> getCatalogsByChannelId(UUID channelId) {
        return channelCatalogRepository.findByChannelId(channelId).stream()
                .map(ChannelCatalog::getCatalog)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getChannelsByCatalogId(UUID catalogId) {
        return channelCatalogRepository.findByCatalogId(catalogId).stream()
                .map(ChannelCatalog::getChannel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChannelCatalog> getActiveChannelCatalogs() {
        return channelCatalogRepository.findByIsEnabledTrue();
    }

    @Override
    public List<ChannelCatalog> getActiveChannelCatalogsByChannelId(UUID channelId) {
        return channelCatalogRepository.findByChannelIdAndIsEnabledTrue(channelId);
    }

    @Override
    public List<ChannelCatalog> getActiveChannelCatalogsByCatalogId(UUID catalogId) {
        return channelCatalogRepository.findByCatalogIdAndIsEnabledTrue(catalogId);
    }
}