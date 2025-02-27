package com.scaler.dto;

import com.scaler.entity.enums.ChannelType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelDTO extends BaseDTO {
    private String code;
    private String name;
    private String description;
    private String status;
    private ChannelType type;
    private String metadata;
    private Boolean enabled;
    private Integer displayOrder;
    private Boolean visibility;
    private String channelConfig;
}
