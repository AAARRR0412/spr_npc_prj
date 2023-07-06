package com.spr.socialtv.dto;

import com.spr.socialtv.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    // 생성시간, 수정시간 추가
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;
    private String username;
    private UserProfileDto userProfile;
    private String imageUrl;

    // 기본 생성자
    public PostResponseDto() {
    }

    public PostResponseDto(Long id, String title, String content, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    // 생성자
    public PostResponseDto(Long id, String title, String content, LocalDateTime createdAt,
                           LocalDateTime modifiedAt, List<CommentResponseDto> commentList,
                           String username, UserProfileDto userProfile, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.commentList = commentList;
        this.username = username;
        this.userProfile = userProfile;
        this.imageUrl = imageUrl;
    }
}
