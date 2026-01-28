package com.sensprj.leo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BlockedDto {
    private String leoTitle;
    private List<String> missingPrerequisites;
    private String text;
    private String tip;
}