package com.spr.socialtv.dto;

import com.spr.socialtv.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private String username;
    private String password;
    private String self_text;


    public UserResponseDto(String username, String password, String selfText) {
        this.username = username;
        this.password = password;
        this.self_text = selfText;
    }

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.self_text = user.getSelf_text();
    }
}
