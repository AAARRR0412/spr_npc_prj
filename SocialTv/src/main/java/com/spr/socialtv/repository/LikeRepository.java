package com.spr.socialtv.repository;

import com.spr.socialtv.entity.Like;
import com.spr.socialtv.entity.Post;
import com.spr.socialtv.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
}
