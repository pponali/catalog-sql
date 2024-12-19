package com.example.mapper;

import com.example.dto.ClassAttributeAssignmentDTO;
import com.example.entity.ClassAttributeAssignment;
import com.example.entity.ClassificationAttribute;
import com.example.entity.ClassificationClass;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-19T12:34:32+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class ClassAttributeAssignmentMapperImpl implements ClassAttributeAssignmentMapper {

    @Override
    public ClassAttributeAssignmentDTO toDTO(ClassAttributeAssignment assignment) {
        if ( assignment == null ) {
            return null;
        }

        ClassAttributeAssignmentDTO.ClassAttributeAssignmentDTOBuilder classAttributeAssignmentDTO = ClassAttributeAssignmentDTO.builder();

        classAttributeAssignmentDTO.classificationAttribute( assignmentClassificationAttributeCode( assignment ) );
        classAttributeAssignmentDTO.classificationClass( assignmentClassificationClassCode( assignment ) );
        classAttributeAssignmentDTO.attributeType( assignment.getAttributeType() );
        Set<String> set = assignment.getAttributeValues();
        if ( set != null ) {
            classAttributeAssignmentDTO.attributeValues( new LinkedHashSet<String>( set ) );
        }
        classAttributeAssignmentDTO.id( assignment.getId() );
        classAttributeAssignmentDTO.mandatory( assignment.isMandatory() );
        classAttributeAssignmentDTO.multiValued( assignment.isMultiValued() );
        classAttributeAssignmentDTO.unit( assignment.getUnit() );

        return classAttributeAssignmentDTO.build();
    }

    @Override
    public ClassAttributeAssignment toEntity(ClassAttributeAssignmentDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ClassAttributeAssignment.ClassAttributeAssignmentBuilder classAttributeAssignment = ClassAttributeAssignment.builder();

        classAttributeAssignment.classificationAttribute( classAttributeAssignmentDTOToClassificationAttribute( dto ) );
        classAttributeAssignment.classificationClass( classAttributeAssignmentDTOToClassificationClass( dto ) );
        classAttributeAssignment.attributeType( dto.getAttributeType() );
        Set<String> set = dto.getAttributeValues();
        if ( set != null ) {
            classAttributeAssignment.attributeValues( new LinkedHashSet<String>( set ) );
        }
        classAttributeAssignment.id( dto.getId() );
        classAttributeAssignment.mandatory( dto.isMandatory() );
        classAttributeAssignment.multiValued( dto.isMultiValued() );
        classAttributeAssignment.unit( dto.getUnit() );

        return classAttributeAssignment.build();
    }

    @Override
    public List<ClassAttributeAssignmentDTO> toDTOList(List<ClassAttributeAssignment> assignments) {
        if ( assignments == null ) {
            return null;
        }

        List<ClassAttributeAssignmentDTO> list = new ArrayList<ClassAttributeAssignmentDTO>( assignments.size() );
        for ( ClassAttributeAssignment classAttributeAssignment : assignments ) {
            list.add( toDTO( classAttributeAssignment ) );
        }

        return list;
    }

    @Override
    public void updateAssignmentFromDTO(ClassAttributeAssignmentDTO dto, ClassAttributeAssignment assignment) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getAttributeType() != null ) {
            assignment.setAttributeType( dto.getAttributeType() );
        }
        if ( assignment.getAttributeValues() != null ) {
            Set<String> set = dto.getAttributeValues();
            if ( set != null ) {
                assignment.getAttributeValues().clear();
                assignment.getAttributeValues().addAll( set );
            }
        }
        else {
            Set<String> set = dto.getAttributeValues();
            if ( set != null ) {
                assignment.setAttributeValues( new LinkedHashSet<String>( set ) );
            }
        }
        if ( dto.getId() != null ) {
            assignment.setId( dto.getId() );
        }
        assignment.setMandatory( dto.isMandatory() );
        assignment.setMultiValued( dto.isMultiValued() );
        if ( dto.getUnit() != null ) {
            assignment.setUnit( dto.getUnit() );
        }
    }

    private String assignmentClassificationAttributeCode(ClassAttributeAssignment classAttributeAssignment) {
        if ( classAttributeAssignment == null ) {
            return null;
        }
        ClassificationAttribute classificationAttribute = classAttributeAssignment.getClassificationAttribute();
        if ( classificationAttribute == null ) {
            return null;
        }
        String code = classificationAttribute.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    private String assignmentClassificationClassCode(ClassAttributeAssignment classAttributeAssignment) {
        if ( classAttributeAssignment == null ) {
            return null;
        }
        ClassificationClass classificationClass = classAttributeAssignment.getClassificationClass();
        if ( classificationClass == null ) {
            return null;
        }
        String code = classificationClass.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    protected ClassificationAttribute classAttributeAssignmentDTOToClassificationAttribute(ClassAttributeAssignmentDTO classAttributeAssignmentDTO) {
        if ( classAttributeAssignmentDTO == null ) {
            return null;
        }

        ClassificationAttribute.ClassificationAttributeBuilder classificationAttribute = ClassificationAttribute.builder();

        classificationAttribute.code( classAttributeAssignmentDTO.getClassificationAttribute() );

        return classificationAttribute.build();
    }

    protected ClassificationClass classAttributeAssignmentDTOToClassificationClass(ClassAttributeAssignmentDTO classAttributeAssignmentDTO) {
        if ( classAttributeAssignmentDTO == null ) {
            return null;
        }

        ClassificationClass.ClassificationClassBuilder classificationClass = ClassificationClass.builder();

        classificationClass.code( classAttributeAssignmentDTO.getClassificationClass() );

        return classificationClass.build();
    }
}
