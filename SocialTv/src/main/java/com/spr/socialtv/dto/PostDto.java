package com.spr.socialtv.dto;

import com.spr.socialtv.entity.Board;
import com.spr.socialtv.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String writerName;
    private String content;
    private User user;
    private Board board;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String imageKey;

    private Integer likeCount;
    private Integer viewCount;

    private List<CommentResponseDto> comments;

}
