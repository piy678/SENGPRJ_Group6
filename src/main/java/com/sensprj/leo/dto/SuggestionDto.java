package com.sensprj.leo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SuggestionDto {
    private String leoTitle;
    private boolean hasDependencies;
    private String prerequisiteTitle;
    private LocalDateTime prerequisiteCompletedOn;
    private String rationale;
    private boolean ready;
}