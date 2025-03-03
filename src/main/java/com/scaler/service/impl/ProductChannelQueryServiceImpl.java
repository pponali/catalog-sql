package com.scaler.service.impl;

import com.scaler.entity.Channel;
import com.scaler.entity.Product;
import com.scaler.entity.ProductChannel;
import com.scaler.repository.ProductChannelRepository;
import com.scaler.service.ProductChannelQueryService;
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
public class ProductChannelQueryServiceImpl implements ProductChannelQueryService {

    private final ProductChannelRepository productChannelRepository;

    @Override
    public List<ProductChannel> getAllProductChannels() {
        return productChannelRepository.findAll();
    }

    @Override
    public Optional<ProductChannel> getProductChannelById(UUID id) {
        return productChannelRepository.findById(id);
    }

    @Override
    public List<ProductChannel> getProductChannelsByProductId(UUID productId) {
        return productChannelRepository.findByProductId(productId);
    }

    @Override
    public List<ProductChannel> getProductChannelsByChannelId(UUID channelId) {
        return productChannelRepository.findByChannelId(channelId);
    }

    @Override
    public Optional<ProductChannel> getProductChannelByProductAndChannel(UUID productId, UUID channelId) {
        return productChannelRepository.findByProductIdAndChannelId(productId, channelId);
    }

    @Override
    public List<Channel> getChannelsByProductId(UUID productId) {
        return productChannelRepository.findByProductId(productId).stream()
                .map(ProductChannel::getChannel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByChannelId(UUID channelId) {
        return productChannelRepository.findByChannelId(channelId).stream()
                .map(ProductChannel::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByChannelIdAndActive(UUID channelId, boolean isActive) {
        return productChannelRepository.findByChannelIdAndIsEnabled(channelId, isActive).stream()
                .map(ProductChannel::getProduct)
                .collect(Collectors.toList());
    }
}