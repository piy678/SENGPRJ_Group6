package com.sensprj.leo.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveAssessmentsRequest {
    private Long teacherId;
    private List<AssessmentEntry> entries;
}