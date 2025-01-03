package com.scaler.mapper;

import com.scaler.dto.FeatureTemplateDTO;
import com.scaler.entity.FeatureTemplate;
import com.scaler.entity.Unit;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeatureTemplateMapper {
    
    FeatureTemplateMapper INSTANCE = Mappers.getMapper(FeatureTemplateMapper.class);

    @Mapping(target = "unitId", source = "unit.id")
    FeatureTemplateDTO toDTO(FeatureTemplate entity);

    @Mapping(target = "unit", source = "unitId", qualifiedByName = "mapUnit")
    @Mapping(target = "categoryFeatureTemplates", ignore = true)
    FeatureTemplate toEntity(FeatureTemplateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "unit", source = "unitId", qualifiedByName = "mapUnit")
    @Mapping(target = "categoryFeatureTemplates", ignore = true)
    void updateEntityFromDTO(FeatureTemplateDTO dto, @MappingTarget FeatureTemplate entity);

    @Named("mapUnit")
    default Unit mapUnit(UUID unitId) {
        if (unitId == null) {
            return null;
        }
        Unit unit = new Unit();
        unit.setId(unitId);
        return unit;
    }
}
