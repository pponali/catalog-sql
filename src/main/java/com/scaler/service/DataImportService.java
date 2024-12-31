package com.scaler.service;

import com.scaler.dto.ImportResult;
import org.springframework.web.multipart.MultipartFile;

public interface DataImportService {
    ImportResult importData(MultipartFile file);
}
