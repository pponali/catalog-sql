package com.nosql.poc.channel.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditEntry {
    private LocalDateTime timestamp;
    private String action;

    // Getters and setters

}
