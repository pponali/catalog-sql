package com.scaler.service;

import com.scaler.dto.BusinessDTO;
import java.util.List;
import java.util.UUID;

public interface BusinessService {
    BusinessDTO createBusiness(BusinessDTO businessDTO);
    BusinessDTO updateBusiness(UUID id, BusinessDTO businessDTO);
    BusinessDTO getBusiness(UUID id);
    List<BusinessDTO> getAllBusinesses();
    void deleteBusiness(UUID id);
    boolean existsById(UUID id);
}
