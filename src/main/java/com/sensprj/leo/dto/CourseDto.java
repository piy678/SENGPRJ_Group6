package com.sensprj.leo.dto;

import com.sensprj.leo.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;
    private String name;
    private Long teacherId;
    private String teacherName;

    public static CourseDto fromEntity(Course c) {
        CourseDto dto = new CourseDto();
        dto.id = c.getId();
        dto.name = c.getName();
        if (c.getTeacher() != null) {
            dto.teacherId = c.getTeacher().getId();
            dto.teacherName = c.getTeacher().getFirstName() + " " + c.getTeacher().getLastName();
        }
        return dto;
    }
}