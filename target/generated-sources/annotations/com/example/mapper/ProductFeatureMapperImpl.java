package com.example.mapper;

import com.example.dto.ProductFeatureDTO;
import com.example.dto.ProductFeatureValueDTO;
import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.Product;
import com.example.entity.ProductFeature;
import com.example.entity.ProductFeatureValue;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-19T12:34:32+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class ProductFeatureMapperImpl implements ProductFeatureMapper {

    @Autowired
    private ProductFeatureValueMapper productFeatureValueMapper;

    @Override
    public ProductFeatureDTO toDTO(ProductFeature entity) {
        if ( entity == null ) {
            return null;
        }

        ProductFeatureDTO productFeatureDTO = new ProductFeatureDTO();

        productFeatureDTO.setId( entity.getId() );
        productFeatureDTO.setValues( productFeatureValueSetToProductFeatureValueDTOSet( entity.getValues() ) );
        productFeatureDTO.setProductId( entityProductId( entity ) );
        productFeatureDTO.setTemplateCode( entityTemplateCode( entity ) );
        productFeatureDTO.setTemplateName( entityTemplateName( entity ) );
        productFeatureDTO.setAttributeType( entityTemplateAttributeType( entity ) );

        return productFeatureDTO;
    }

    @Override
    public ProductFeature toEntity(ProductFeatureDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductFeature.ProductFeatureBuilder productFeature = ProductFeature.builder();

        productFeature.values( productFeatureValueDTOSetToProductFeatureValueSet( dto.getValues() ) );

        return productFeature.build();
    }

    @Override
    public void updateEntityFromDTO(ProductFeatureDTO dto, ProductFeature entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getValues() != null ) {
            Set<ProductFeatureValue> set = productFeatureValueDTOSetToProductFeatureValueSet( dto.getValues() );
            if ( set != null ) {
                entity.getValues().clear();
                entity.getValues().addAll( set );
            }
            else {
                entity.setValues( null );
            }
        }
        else {
            Set<ProductFeatureValue> set = productFeatureValueDTOSetToProductFeatureValueSet( dto.getValues() );
            if ( set != null ) {
                entity.setValues( set );
            }
        }
    }

    protected Set<ProductFeatureValueDTO> productFeatureValueSetToProductFeatureValueDTOSet(Set<ProductFeatureValue> set) {
        if ( set == null ) {
            return null;
        }

        Set<ProductFeatureValueDTO> set1 = new LinkedHashSet<ProductFeatureValueDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ProductFeatureValue productFeatureValue : set ) {
            set1.add( productFeatureValueMapper.toDTO( productFeatureValue ) );
        }

        return set1;
    }

    private Long entityProductId(ProductFeature productFeature) {
        if ( productFeature == null ) {
            return null;
        }
        Product product = productFeature.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityTemplateCode(ProductFeature productFeature) {
        if ( productFeature == null ) {
            return null;
        }
        CategoryFeatureTemplate template = productFeature.getTemplate();
        if ( template == null ) {
            return null;
        }
        String code = template.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    private String entityTemplateName(ProductFeature productFeature) {
        if ( productFeature == null ) {
            return null;
        }
        CategoryFeatureTemplate template = productFeature.getTemplate();
        if ( template == null ) {
            return null;
        }
        String name = template.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityTemplateAttributeType(ProductFeature productFeature) {
        if ( productFeature == null ) {
            return null;
        }
        CategoryFeatureTemplate template = productFeature.getTemplate();
        if ( template == null ) {
            return null;
        }
        String attributeType = template.getAttributeType();
        if ( attributeType == null ) {
            return null;
        }
        return attributeType;
    }

    protected Set<ProductFeatureValue> productFeatureValueDTOSetToProductFeatureValueSet(Set<ProductFeatureValueDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<ProductFeatureValue> set1 = new LinkedHashSet<ProductFeatureValue>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ProductFeatureValueDTO productFeatureValueDTO : set ) {
            set1.add( productFeatureValueMapper.toEntity( productFeatureValueDTO ) );
        }

        return set1;
    }
}
