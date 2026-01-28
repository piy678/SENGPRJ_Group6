package com.sensprj.leo.dto;

import lombok.Data;

@Data
public class CreateLeoRequest {
    private String title;
    private String description;
    private Long prerequisiteLeoId;
    private Long createdById;
}