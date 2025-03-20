package com.nosql.poc.channel.legacy;

import com.nosql.poc.channel.dto.MerchantDTO;
import java.util.List;
import java.util.UUID;

public interface BusinessService {
    MerchantDTO createBusiness(MerchantDTO merchantDTO);
    MerchantDTO updateBusiness(UUID id, MerchantDTO merchantDTO);
    MerchantDTO getMerchant(UUID id);
    List<MerchantDTO> getAllBusinesses();
    void deleteBusiness(UUID id);
    boolean existsById(UUID id);
}
