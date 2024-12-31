package com.scaler.service.impl;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import com.scaler.mapper.ProductMapper;
import com.scaler.repository.ProductRepository;
import com.scaler.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        Product entity = mapper.toEntity(dto);
        entity.setId(UUID.randomUUID());
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional
    public ProductDTO update(UUID id, ProductDTO dto) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        Product entity = repository.findById(id).orElseThrow();
        mapper.updateEntity(entity, dto);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
