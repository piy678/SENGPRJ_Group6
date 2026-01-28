package com.sensprj.leo.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCourseRequest {
    private Long teacherId;
    private String name;
    private List<Long> studentIds;
}