package com.spr.socialtv.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostDto {
    private Long postId;
    private String title;
    private String writerName;
    private String content;
}
