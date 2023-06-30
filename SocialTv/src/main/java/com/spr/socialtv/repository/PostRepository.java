package com.spr.socialtv.repository;

import com.spr.socialtv.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
