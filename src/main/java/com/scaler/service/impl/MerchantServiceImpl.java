package com.scaler.service.impl;

import com.scaler.dto.MerchantDTO;
import com.scaler.entity.Merchant;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.MerchantMapper;
import com.scaler.repository.MerchantRepository;
import com.scaler.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Override
    @Transactional
    public MerchantDTO createBusiness(MerchantDTO MerchantDTO) {
        Merchant merchant = merchantMapper.toEntity(MerchantDTO);
        merchant = merchantRepository.save(merchant);
        return merchantMapper.toDTO(merchant);
    }

    @Override
    @Transactional
    public MerchantDTO updateBusiness(UUID id, MerchantDTO MerchantDTO) {
        Merchant merchant = merchantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + id));
        
        merchant.setName(MerchantDTO.getName());
        merchant.setDescription(MerchantDTO.getDescription());

        merchant = merchantRepository.save(merchant);
        return merchantMapper.toDTO(merchant);
    }

    @Override
    @Transactional(readOnly = true)
    public MerchantDTO getMerchant(UUID id) {
        Merchant merchant = merchantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Merchant not found with id: " + id));
        return merchantMapper.toDTO(merchant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MerchantDTO> getAllBusinesses() {
        return merchantRepository.findAll().stream()
            .map(merchantMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBusiness(UUID id) {
        if (!merchantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Merchant not found with id: " + id);
        }
        merchantRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return merchantRepository.existsById(id);
    }
}
