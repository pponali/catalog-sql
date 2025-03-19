package com.scaler.mapper;

import com.scaler.dto.ChannelDTO;
import com.scaler.entity.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ChannelMapperDecorator implements ChannelMapper {

    @Autowired
    @Qualifier("delegate")
    private ChannelMapper delegate;

    @Override
    public ChannelDTO toDTO(Channel entity) {
        if (entity == null) {
            return null;
        }
        return delegate.toDTO(entity);
    }

    @Override
    public Channel toEntity(ChannelDTO dto) {
        if (dto == null) {
            return null;
        }
        return delegate.toEntity(dto);
    }

    @Override
    public void updateEntity(Channel entity, ChannelDTO dto) {
        if (entity == null || dto == null) {
            return;
        }
        delegate.updateEntity(entity, dto);
    }

    @Override
    public List<ChannelDTO> toDTOList(List<Channel> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Set<ChannelDTO> toDTOSet(Set<Channel> entities) {
        if (entities == null) {
            return new HashSet<>();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Channel> toEntityList(List<ChannelDTO> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Channel> toEntitySet(Set<ChannelDTO> dtos) {
        if (dtos == null) {
            return new HashSet<>();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}
