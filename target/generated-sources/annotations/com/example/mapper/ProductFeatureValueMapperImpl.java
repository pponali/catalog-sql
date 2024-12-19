package com.example.mapper;

import com.example.dto.ProductFeatureValueDTO;
import com.example.entity.ProductFeatureValue;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-19T12:34:32+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class ProductFeatureValueMapperImpl implements ProductFeatureValueMapper {

    @Override
    public ProductFeatureValueDTO toDTO(ProductFeatureValue entity) {
        if ( entity == null ) {
            return null;
        }

        ProductFeatureValueDTO productFeatureValueDTO = new ProductFeatureValueDTO();

        productFeatureValueDTO.setId( entity.getId() );
        productFeatureValueDTO.setValue( entity.getValue() );
        productFeatureValueDTO.setUnit( entity.getUnit() );

        return productFeatureValueDTO;
    }

    @Override
    public ProductFeatureValue toEntity(ProductFeatureValueDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductFeatureValue.ProductFeatureValueBuilder productFeatureValue = ProductFeatureValue.builder();

        productFeatureValue.unit( dto.getUnit() );
        productFeatureValue.value( dto.getValue() );

        return productFeatureValue.build();
    }

    @Override
    public void updateEntityFromDTO(ProductFeatureValueDTO dto, ProductFeatureValue entity) {
        if ( dto == null ) {
            return;
        }

        entity.setUnit( dto.getUnit() );
        entity.setValue( dto.getValue() );
    }
}
