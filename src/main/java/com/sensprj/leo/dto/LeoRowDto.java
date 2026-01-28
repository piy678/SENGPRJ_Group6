package com.sensprj.leo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeoRowDto {
    private Long leoId;
    private String title;
    private String dependsOn;
    private String status;
    private LocalDateTime lastUpdated;
}