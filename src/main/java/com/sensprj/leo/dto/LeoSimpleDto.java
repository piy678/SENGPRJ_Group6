package com.sensprj.leo.dto;

import com.sensprj.leo.controller.AssessmentController;
import com.sensprj.leo.entity.Leo;
import lombok.Data;

@Data
public class LeoSimpleDto {
    private Long id;
    private String title;
    public static LeoSimpleDto fromEntity(Leo leo) {
        LeoSimpleDto dto = new LeoSimpleDto();
        dto.id = leo.getId();
        dto.title = leo.getName();
        return dto;
    }
}