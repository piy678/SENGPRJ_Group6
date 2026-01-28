package com.sensprj.leo.dto;

import com.sensprj.leo.controller.AuthController;
import com.sensprj.leo.entity.User;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String role;
    private String firstName;
    private String lastName;

    public static UserDto fromEntity(User u) {
        UserDto dto = new UserDto();
        dto.id = u.getId();
        dto.username = u.getUsername();
        dto.role = u.getRole();
        dto.firstName = u.getFirstName();
        dto.lastName = u.getLastName();
        return dto;
    }
}
