package com.spr.socialtv.repository;

import com.spr.socialtv.entity.Comment;
import com.spr.socialtv.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByPostOrderByCreateDateDesc(Post post);
}
