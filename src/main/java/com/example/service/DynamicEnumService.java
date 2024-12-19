package com.example.service;

import com.example.entity.EnumValue;
import com.example.repository.EnumValueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DynamicEnumService {

    private final EnumValueRepository enumValueRepository;

    @Cacheable(value = "enumValues", key = "#enumType")
    public List<EnumValue> getEnumValues(String enumType) {
        return enumValueRepository.findByEnumTypeAndActiveOrderBySortOrder(enumType, true);
    }

    @Cacheable(value = "enumValues", key = "#enumType + '_' + #code")
    public Optional<EnumValue> getEnumValue(String enumType, String code) {
        return enumValueRepository.findByEnumTypeAndCodeAndActive(enumType, code, true);
    }

    @Transactional
    public EnumValue createEnumValue(String enumType, String code, String name, String description, Integer sortOrder) {
        EnumValue enumValue = EnumValue.builder()
            .enumType(enumType)
            .code(code)
            .name(name)
            .description(description)
            .sortOrder(sortOrder)
            .active(true)
            .build();
        return enumValueRepository.save(enumValue);
    }

    @Transactional
    public void deactivateEnumValue(String enumType, String code) {
        enumValueRepository.findByEnumTypeAndCode(enumType, code)
            .ifPresent(enumValue -> {
                enumValue.setActive(false);
                enumValueRepository.save(enumValue);
            });
    }

    public boolean isValidEnumValue(String enumType, String code) {
        return enumValueRepository.existsByEnumTypeAndCodeAndActive(enumType, code, true);
    }

    public boolean areValidEnumValues(String enumType, List<String> codes) {
        return codes.stream().allMatch(code -> isValidEnumValue(enumType, code));
    }
}
