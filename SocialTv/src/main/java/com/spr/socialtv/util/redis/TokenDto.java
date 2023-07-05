package com.spr.socialtv.util.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class TokenDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }
}