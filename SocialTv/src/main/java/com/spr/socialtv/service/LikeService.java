package com.spr.socialtv.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.spr.socialtv.dto.ApiResponseDto;
import com.spr.socialtv.dto.LikeRequestDTO;
import com.spr.socialtv.entity.Like;
import com.spr.socialtv.entity.Post;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.repository.LikeRepository;
import com.spr.socialtv.repository.PostRepository;
import com.spr.socialtv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /*
    * 좋아요
    * */
    @Transactional
    public ResponseEntity<ApiResponseDto> insert(LikeRequestDTO likeRequestDTO) throws Exception {

        User user = userRepository.findById(likeRequestDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("Could not found User id : " + likeRequestDTO.getUserId()));

        Post post = postRepository.findById(likeRequestDTO.getPostId())
                .orElseThrow(() -> new NotFoundException("Could not found Post id : " + likeRequestDTO.getPostId()));

        // 이미 좋아요되어있으면 에러 반환
        if (likeRepository.findByUserAndPost(user, post).isPresent()) {
            //TODO 409에러로 변경
            return ResponseEntity.ok().body(new ApiResponseDto("이미 좋아요를 누른 글입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        likeRepository.save(like);
        postRepository.addLikeCount(post);
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }

    /*
    * 좋아요 취소
    * */
    @Transactional
    public void delete(LikeRequestDTO likeRequestDTO) {

        User user = userRepository.findById(likeRequestDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "좋아요를 누른 사용자를 찾을 수 없습니다."));
        //  new NotFoundException("Could not found User id : " + likeRequestDTO.getUserId()));

        Post post = postRepository.findById(likeRequestDTO.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        //new NotFoundException("Could not found Post id : " + likeRequestDTO.getPostId()));

        Like like = (Like) likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "좋아요를 누른 기록을 찾을 수 없습니다."));
        // new NotFoundException("Could not found like id"));

        likeRepository.delete(like);
        postRepository.subLikeCount(post);
    }
}
