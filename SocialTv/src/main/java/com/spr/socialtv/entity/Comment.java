package com.spr.socialtv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "comment")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 댓글 내용


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 관리자 사용자 접근권한 때문에 작성자는 업데이트 하지 않음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable=false)
    private User user; // 작성자

    @Override
    public LocalDateTime getUpdateDate() {
        return super.getUpdateDate();
    }

    @Override
    public LocalDateTime getCreateDate() {
        return super.getCreateDate();
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    public void setUser(User user) {
        this.user = user;
    }
}

