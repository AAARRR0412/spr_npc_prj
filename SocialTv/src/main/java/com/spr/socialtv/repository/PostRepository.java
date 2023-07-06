package com.spr.socialtv.repository;

import com.spr.socialtv.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    Optional<Post> findByIdAndUserId(Long postId, Long userId);
    List<Post> findByUserId(Long userId);
}
