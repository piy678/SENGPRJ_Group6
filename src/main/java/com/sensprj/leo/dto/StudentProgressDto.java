package com.sensprj.leo.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentProgressDto {
    private Long studentId;
    private String name;
    private int totalLeos;
    private int achieved;
    private int partially;
    private int unmarked;
    private int notAchieved;
    private int total;
    private double progress;

    private List<SuggestionDto> suggestions;
    private List<LeoRowDto> leoStatuses;
    private List<BlockedDto> blocked;

}
