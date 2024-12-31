package com.scaler.mapper;

import com.scaler.dto.ClassAttributeAssignmentDTO;
import com.scaler.entity.ClassAttributeAssignment;
import com.scaler.entity.ClassificationAttribute;
import com.scaler.entity.ClassificationClass;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassAttributeAssignmentMapper {

    @Mapping(target = "classificationAttribute", source = "classificationAttribute.code")
    @Mapping(target = "classificationClass", source = "classificationClass.code")
    ClassAttributeAssignmentDTO toDTO(ClassAttributeAssignment assignment);

    @Mapping(target = "classificationAttribute", expression = "java(createClassificationAttribute(dto.getClassificationAttribute()))")
    @Mapping(target = "classificationClass", expression = "java(createClassificationClass(dto.getClassificationClass()))")
    ClassAttributeAssignment toEntity(ClassAttributeAssignmentDTO dto);

    List<ClassAttributeAssignmentDTO> toDTOList(List<ClassAttributeAssignment> assignments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "classificationAttribute", ignore = true)
    @Mapping(target = "classificationClass", ignore = true)
    void updateAssignmentFromDTO(ClassAttributeAssignmentDTO dto, @MappingTarget ClassAttributeAssignment assignment);

    default ClassificationAttribute createClassificationAttribute(String code) {
        if (code == null) {
            return null;
        }
        return ClassificationAttribute.builder().code(code).build();
    }

    default ClassificationClass createClassificationClass(String code) {
        if (code == null) {
            return null;
        }
        return ClassificationClass.builder().code(code).build();
    }
}
