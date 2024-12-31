package com.scaler.service.impl;

import com.scaler.dto.BusinessDTO;
import com.scaler.entity.Business;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.BusinessMapper;
import com.scaler.repository.BusinessRepository;
import com.scaler.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {
    
    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    @Override
    @Transactional
    public BusinessDTO createBusiness(BusinessDTO businessDTO) {
        Business business = businessMapper.toEntity(businessDTO);
        business = businessRepository.save(business);
        return businessMapper.toDTO(business);
    }

    @Override
    @Transactional
    public BusinessDTO updateBusiness(UUID id, BusinessDTO businessDTO) {
        Business business = businessRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + id));
        
        business.setName(businessDTO.getName());
        business.setDescription(businessDTO.getDescription());
        
        business = businessRepository.save(business);
        return businessMapper.toDTO(business);
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessDTO getBusiness(UUID id) {
        Business business = businessRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Business not found with id: " + id));
        return businessMapper.toDTO(business);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusinessDTO> getAllBusinesses() {
        return businessRepository.findAll().stream()
            .map(businessMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBusiness(UUID id) {
        if (!businessRepository.existsById(id)) {
            throw new ResourceNotFoundException("Business not found with id: " + id);
        }
        businessRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return businessRepository.existsById(id);
    }
}
