package com.nosql.poc.catalog.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scaler.config.CustomDateSerializer;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
public abstract class BaseEntity {
    private UUID id;

    @CreatedDate
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
