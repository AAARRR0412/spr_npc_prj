package com.spr.socialtv.repository;

import com.spr.socialtv.entity.Post;

public interface CustomPostRepository {

    // 좋아요
    void addLikeCount(Post post);

    //싫어요
    void subLikeCount(Post post);
}
