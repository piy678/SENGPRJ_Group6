package com.sensprj.leo.dto;

import com.sensprj.leo.entity.User;
import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String name;

    public static StudentDto fromEntity(User u) {
        StudentDto dto = new StudentDto();
        dto.setId(u.getId());
        dto.setName(u.getFirstName() + " " + u.getLastName());
        return dto;
    }
}
