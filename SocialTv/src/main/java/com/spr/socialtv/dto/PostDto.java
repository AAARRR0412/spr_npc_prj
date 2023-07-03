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
    private String writerName; //user에 있는 작성자 참고
    private String comment;
    private String postPassword;
}
