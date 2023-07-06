package com.spr.socialtv.dto;

import com.spr.socialtv.entity.Post;
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

    // 들어온 값으로 필드들을 조회해서 넣어주게 됨
    // 게시글 조회, 생성, 수정에서 사용
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreateDate();
        this.modifiedAt = post.getUpdateDate();
        this.username = post.getUser().getUsername();
        this.commentList = post.getComments().stream().map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed()) // 작성날짜 내림차순
                .toList();
    }
}
