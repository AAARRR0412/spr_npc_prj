package com.spr.socialtv.dto;


import com.spr.socialtv.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private long commentId;
    private long postId;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(long commentId, long postId, String comment, String username) {
        this.commentId = commentId;
        this.postId = postId;
        this.comment = comment;
        this.username = username;
    }

    // Entity -> Dto
    public CommentResponseDto(Comment comment) {
        this.comment = comment.getComment();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreateDate();
        this.modifiedAt = comment.getUpdateDate();

    }
}