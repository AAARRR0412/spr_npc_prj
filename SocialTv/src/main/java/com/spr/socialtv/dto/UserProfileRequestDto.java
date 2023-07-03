package com.spr.socialtv.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileRequestDto {
    private String username;
    private String email;
}
