package com.spr.socialtv.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다. */
public class Post extends Timestamped{

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*
    @Column(length = 10, nullable = false)
    private String author;
*/

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // @Column(length = 255, nullable = false)
    // private String postPassword;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("id asc") // 댓글 정렬
    private List<Comment> comments;

    // 관리자 사용자 접근권한 때문에 작성자는 업데이트 하지 않음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable=false)
    private User user; // 작성자


    @Builder
    public Post(Long id,String title, String content, String postPassword, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        // this.postPassword = postPassword;
        this.user = user;
    }

    public Long getId() {
        return id;
    }
}
