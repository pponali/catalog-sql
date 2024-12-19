package com.example.mapper;

import com.example.dto.ClassAttributeAssignmentDTO;
import com.example.entity.ClassAttributeAssignment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassAttributeAssignmentMapper {

    @Mapping(target = "classificationAttribute", source = "classificationAttribute.code")
    @Mapping(target = "classificationClass", source = "classificationClass.code")
    ClassAttributeAssignmentDTO toDTO(ClassAttributeAssignment assignment);

    @Mapping(target = "classificationAttribute.code", source = "classificationAttribute")
    @Mapping(target = "classificationClass.code", source = "classificationClass")
    ClassAttributeAssignment toEntity(ClassAttributeAssignmentDTO dto);

    List<ClassAttributeAssignmentDTO> toDTOList(List<ClassAttributeAssignment> assignments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "classificationAttribute", ignore = true)
    @Mapping(target = "classificationClass", ignore = true)
    void updateAssignmentFromDTO(ClassAttributeAssignmentDTO dto, @MappingTarget ClassAttributeAssignment assignment);
}
