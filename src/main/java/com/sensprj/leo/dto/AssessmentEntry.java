package com.sensprj.leo.dto;

import com.sensprj.leo.entity.enums.AssessmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssessmentEntry {
    private Long leoId;
    private AssessmentStatus status;
    private LocalDateTime assessedAt;
}
