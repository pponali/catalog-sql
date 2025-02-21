package com.nosql.poc.channel.service;

import com.nosql.poc.channel.model.*;
import com.nosql.poc.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChannelService {
    
    private final ChannelRepository channelRepository;
    private final ChannelValidationService validationService;
    private final ChannelIntegrationService integrationService;
    
    public Channel createChannel(Channel channel) {
        // Validate channel configuration
        validationService.validateChannelConfiguration(channel);
        
        // Initialize metrics
        ChannelMetrics metrics = new ChannelMetrics();
        metrics.setLastSync(LocalDateTime.now());
        channel.setMetrics(metrics);
        
        // Set audit info
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setCreatedDate(LocalDateTime.now());
        channel.setAuditInfo(auditInfo);
        
        // Test integration if configured
        if (channel.getIntegrations() != null && !channel.getIntegrations().isEmpty()) {
            integrationService.testIntegrations(channel);
        }
        
        return channelRepository.save(channel);
    }
    
    public Channel updateChannel(String id, Channel channel) {
        Channel existingChannel = channelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
        
        // Validate updated configuration
        validationService.validateChannelConfiguration(channel);
        
        // Update audit info
        AuditInfo auditInfo = existingChannel.getAuditInfo();
        auditInfo.setLastModifiedDate(LocalDateTime.now());
        channel.setAuditInfo(auditInfo);
        
        // Test integration if changed
        if (channel.getIntegrations() != null && !channel.getIntegrations().isEmpty()) {
            integrationService.testIntegrations(channel);
        }
        
        channel.setId(id);
        return channelRepository.save(channel);
    }
    
    public void activateChannel(String id) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
            
        // Validate channel can be activated
        validationService.validateChannelActivation(channel);
        
        channel.setActive(true);
        
        // Update audit
        AuditInfo auditInfo = channel.getAuditInfo();
        AuditEntry entry = new AuditEntry();
        entry.setTimestamp(LocalDateTime.now());
        entry.setAction("CHANNEL_ACTIVATION");
        auditInfo.getAuditTrail().add(entry);
        
        channelRepository.save(channel);
    }
    
    public void deactivateChannel(String id) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
            
        channel.setActive(false);
        
        // Update audit
        AuditInfo auditInfo = channel.getAuditInfo();
        AuditEntry entry = new AuditEntry();
        entry.setTimestamp(LocalDateTime.now());
        entry.setAction("CHANNEL_DEACTIVATION");
        auditInfo.getAuditTrail().add(entry);
        
        channelRepository.save(channel);
    }
    
    public void updateChannelMetrics(String id, ChannelMetrics metrics) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
            
        channel.setMetrics(metrics);
        
        // Update audit
        AuditInfo auditInfo = channel.getAuditInfo();
        AuditEntry entry = new AuditEntry();
        entry.setTimestamp(LocalDateTime.now());
        entry.setAction("METRICS_UPDATE");
        auditInfo.getAuditTrail().add(entry);
        
        channelRepository.save(channel);
    }
    
    public List<Channel> getChannelsByType(String type) {
        return channelRepository.findByType(type);
    }
    
    public List<Channel> getActiveChannels() {
        return channelRepository.findByActive(true);
    }
    
    public List<Channel> getChannelsByEntityId(String entityId) {
        return channelRepository.findByEntityId(entityId);
    }
    
    public List<Channel> getChannelsByMinimumQualityScore(double minScore) {
        return channelRepository.findByMinimumQualityScore(minScore);
    }
}
