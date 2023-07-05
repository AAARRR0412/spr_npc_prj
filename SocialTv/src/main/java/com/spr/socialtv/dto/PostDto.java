package com.spr.socialtv.dto;

import com.spr.socialtv.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String writerName;
    private String content;
    private User user;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String imageKey;
}
