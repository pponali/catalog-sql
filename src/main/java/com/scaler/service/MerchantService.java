package com.scaler.service;

import com.scaler.dto.MerchantDTO;
import java.util.List;
import java.util.UUID;

public interface MerchantService {
    MerchantDTO createBusiness(MerchantDTO MerchantDTO);
    MerchantDTO updateBusiness(UUID id, MerchantDTO MerchantDTO);
    MerchantDTO getMerchant(UUID id);
    List<MerchantDTO> getAllBusinesses();
    void deleteBusiness(UUID id);
    boolean existsById(UUID id);
}
