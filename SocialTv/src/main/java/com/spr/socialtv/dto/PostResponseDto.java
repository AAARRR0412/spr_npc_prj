package com.spr.socialtv.dto;

import com.spr.socialtv.entity.Comment;
import com.spr.socialtv.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Comment> commentList;
    private String username;
    private String userProfile;
    private String imageUrl;
    private String profileImageUrl;
}
