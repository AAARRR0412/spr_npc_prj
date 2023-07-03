package com.spr.socialtv.dto;

import com.spr.socialtv.entity.UserRoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private UserRoleEnum role;


    public UserDto(){}
    public UserDto(Long id, String username, String email, UserRoleEnum role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}