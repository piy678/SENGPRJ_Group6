package com.sensprj.leo.dto;

import com.sensprj.leo.entity.Leo;
import lombok.Data;

@Data
public class LeoDto {
    private Long id;
    private String name;
    private String description;
    private String topic;
    private Boolean isActive;
    private Long prerequisiteCount;

    public static LeoDto fromEntity(Leo leo) {
        LeoDto dto = new LeoDto();
        dto.id = leo.getId();
        dto.name = leo.getName();
        dto.description = leo.getDescription();
        dto.topic = leo.getTopic();
        dto.isActive = leo.getIsActive();
        return dto;
    }
}
