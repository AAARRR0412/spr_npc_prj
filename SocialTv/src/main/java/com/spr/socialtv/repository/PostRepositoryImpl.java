package com.spr.socialtv.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spr.socialtv.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.spr.socialtv.entity.QPost.post;


// 게시글 관리 repository
@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory queryFactory;

    // 좋아요 추가
    @Override
    public void addLikeCount(Post selectedPost) {
        queryFactory.update(post)
                .set(post.likeCount, post.likeCount.add(1))
                .where(post.eq(selectedPost))
                .execute();
    }

    // 싫어요 
    @Override
    public void subLikeCount(Post selectedPost) {
        queryFactory.update(post)
                .set(post.likeCount, post.likeCount.subtract(1))
                .where(post.eq(selectedPost))
                .execute();
    }
}
