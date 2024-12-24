package com.scaler.repository;

import com.scaler.entity.EnumValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnumValueRepository extends JpaRepository<EnumValue, Long> {
    
    List<EnumValue> findByEnumTypeAndActiveOrderBySortOrder(String enumType, boolean active);
    
    Optional<EnumValue> findByEnumTypeAndCodeAndActive(String enumType, String code, boolean active);
    
    Optional<EnumValue> findByEnumTypeAndCode(String enumType, String code);
    
    boolean existsByEnumTypeAndCodeAndActive(String enumType, String code, boolean active);
    
    List<EnumValue> findByEnumTypeAndCodeInAndActive(String enumType, List<String> codes, boolean active);
}
