package com.example.mapper;

import com.example.dto.ProductDTO;
import com.example.dto.ProductFeatureDTO;
import com.example.entity.Product;
import com.example.entity.ProductFeature;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-19T12:35:19+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private ProductFeatureMapper productFeatureMapper;

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.active( product.isActive() );
        productDTO.code( product.getCode() );
        productDTO.description( product.getDescription() );
        productDTO.features( productFeatureSetToProductFeatureDTOSet( product.getFeatures() ) );
        productDTO.id( product.getId() );
        productDTO.name( product.getName() );

        return productDTO.build();
    }

    @Override
    public Product toEntity(ProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        product.setActive( dto.isActive() );
        product.setCode( dto.getCode() );
        product.setDescription( dto.getDescription() );
        product.setId( dto.getId() );
        product.setName( dto.getName() );

        return product;
    }

    protected Set<ProductFeatureDTO> productFeatureSetToProductFeatureDTOSet(Set<ProductFeature> set) {
        if ( set == null ) {
            return null;
        }

        Set<ProductFeatureDTO> set1 = new LinkedHashSet<ProductFeatureDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ProductFeature productFeature : set ) {
            set1.add( productFeatureMapper.toDTO( productFeature ) );
        }

        return set1;
    }
}
