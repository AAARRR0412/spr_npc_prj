package com.spr.socialtv.dto;

import com.spr.socialtv.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private String selfText;


    public UserResponseDto(String selfText) {
        this.selfText = selfText;
    }

    public UserResponseDto(User user) {
        this.selfText = user.getSelfText();
    }
}
