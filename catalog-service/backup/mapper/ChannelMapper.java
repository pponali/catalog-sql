package com.scaler.mapper;

import com.scaler.dto.ChannelDTO;
import com.scaler.entity.Channel;
import com.scaler.entity.enums.ChannelType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {UUID.class, ChannelType.class})
@DecoratedWith(ChannelMapperDecorator.class)
public interface ChannelMapper {
    
    ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

    @Mappings({
        @Mapping(target = "code", source = "code"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "description", source = "description"),
        @Mapping(target = "status", source = "status"),
        @Mapping(target = "type", source = "type"),
        @Mapping(target = "metadata", source = "metadata"),
        @Mapping(target = "enabled", source = "enabled"),
        @Mapping(target = "displayOrder", source = "displayOrder"),
        @Mapping(target = "visibility", source = "visibility"),
        @Mapping(target = "channelConfig", source = "channelConfig")
    })
    ChannelDTO toDTO(Channel entity);

    @Mappings({
        @Mapping(target = "store", ignore = true)
    })
    Channel toEntity(ChannelDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
        @Mapping(target = "store", ignore = true)
    })
    void updateEntity(@MappingTarget Channel entity, ChannelDTO dto);

    List<ChannelDTO> toDTOList(List<Channel> entities);
    Set<ChannelDTO> toDTOSet(Set<Channel> entities);

    List<Channel> toEntityList(List<ChannelDTO> dtos);
    Set<Channel> toEntitySet(Set<ChannelDTO> dtos);
}
