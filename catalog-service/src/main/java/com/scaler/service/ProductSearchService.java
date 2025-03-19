package com.scaler.service;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.ProductSearchCriteria;
import com.scaler.entity.Product;
import com.scaler.mapper.ProductMapper;
import com.scaler.repository.ProductRepository;
import com.scaler.repository.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    public Page<ProductDTO> searchProducts(ProductSearchCriteria criteria) {
        // Create pageable with sorting
        Pageable pageable = createPageable(criteria);
        
        // Create specification from criteria
        Specification<Product> specification = ProductSpecification.withSearchCriteria(criteria);
        
        // Execute search with specification and pagination
        return productRepository.findAll(specification, pageable)
            .map(productMapper::toDTO);
    }
    
    private Pageable createPageable(ProductSearchCriteria criteria) {
        // Default values
        int page = criteria.getPage() != null ? criteria.getPage() : 0;
        int size = criteria.getSize() != null ? criteria.getSize() : 20;
        
        // Handle sorting
        if (criteria.getSortBy() != null && !criteria.getSortBy().isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC;
            if (criteria.getSortDirection() != null && 
                criteria.getSortDirection().equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }
            return PageRequest.of(page, size, Sort.by(direction, criteria.getSortBy()));
        }
        
        return PageRequest.of(page, size);
    }
}
