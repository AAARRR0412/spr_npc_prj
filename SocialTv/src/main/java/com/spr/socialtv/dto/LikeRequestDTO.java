package com.spr.socialtv.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeRequestDTO {
    private Long userId;
    private Long postId;

    public LikeRequestDTO(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
