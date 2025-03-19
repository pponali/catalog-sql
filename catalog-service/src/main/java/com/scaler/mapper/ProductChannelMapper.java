package com.scaler.mapper;

import com.scaler.dto.ProductChannelDTO;
import com.scaler.entity.ProductChannel;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class, ChannelMapper.class})
public interface ProductChannelMapper {

    @Mappings({
            @Mapping(target = "productId", source = "product.id"),
            @Mapping(target = "channelId", source = "channel.id")
    })
    ProductChannelDTO toDTO(ProductChannel entity);

    @Mappings({
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "channel", ignore = true)
    })
    ProductChannel toEntity(ProductChannelDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "channel", ignore = true)
    })
    void updateEntity(@MappingTarget ProductChannel entity, ProductChannelDTO dto);
}
