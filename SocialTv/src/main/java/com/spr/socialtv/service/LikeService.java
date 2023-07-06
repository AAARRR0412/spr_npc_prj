package com.spr.socialtv.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.spr.socialtv.dto.LikeRequestDTO;
import com.spr.socialtv.entity.Like;
import com.spr.socialtv.entity.Post;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.repository.LikeRepository;
import com.spr.socialtv.repository.PostRepository;
import com.spr.socialtv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void insert(LikeRequestDTO likeRequestDTO) throws Exception {

        User user = userRepository.findById(likeRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Could not found User id : " + likeRequestDTO.getUserId()));

        Post post = postRepository.findById(likeRequestDTO.getPostId())
                .orElseThrow(() -> new NotFoundException("Could not found Post id : " + likeRequestDTO.getPostId()));

        // 이미 좋아요되어있으면 에러 반환
        if (likeRepository.findByUserAndPost(user, post).isPresent()){
            //TODO 409에러로 변경
            throw new Exception();
        }

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        likeRepository.save(like);
        postRepository.addLikeCount(post);
    }

    @Transactional
    public void delete(LikeRequestDTO likeRequestDTO) {

        User user = userRepository.findById(likeRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Could not found User id : " + likeRequestDTO.getUserId()));

        Post post = postRepository.findById(likeRequestDTO.getPostId())
                .orElseThrow(() -> new NotFoundException("Could not found Post id : " + likeRequestDTO.getPostId()));

        Like like = (Like) likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new NotFoundException("Could not found like id"));

        likeRepository.delete(like);
        postRepository.subLikeCount(post);
    }
}
