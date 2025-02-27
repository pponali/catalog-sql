package com.scaler.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductChannelDTO extends BaseDTO {
    private UUID productId;
    private UUID channelId;
    private Boolean isEnabled;
    private Boolean isVisible;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private String channelMetadata;
}
