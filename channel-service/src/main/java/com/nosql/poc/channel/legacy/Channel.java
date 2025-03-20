package com.nosql.poc.channel.legacy;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Legacy Channel model - kept for backward compatibility
 */
@Document(collection = "legacy_channel")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Id
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
