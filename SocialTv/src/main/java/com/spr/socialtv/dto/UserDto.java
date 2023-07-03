package com.spr.socialtv.dto;

import com.spr.socialtv.entity.UserRoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private UserRoleEnum role;


    public UserDto(){}
    public UserDto(Long userId, String username, String email, UserRoleEnum role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}