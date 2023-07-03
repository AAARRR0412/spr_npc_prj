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
@Table(name = "comments")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment; // 댓글 내용


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // 관리자 사용자 접근권한 때문에 작성자는 업데이트 하지 않음
    @ManyToOne
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
    public void setContent(String comment) {
        this.comment = comment;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    public void setUser(User user) {
        this.user = user;
    }
}

