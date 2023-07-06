package com.spr.socialtv.controller;


import com.spr.socialtv.dto.ApiResponseDto;
import com.spr.socialtv.dto.CommentRequestDto;
import com.spr.socialtv.dto.CommentResponseDto;
import com.spr.socialtv.security.UserDetailsImpl;
import com.spr.socialtv.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

/**
* 댓글
* */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("{post_id}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto, @PathVariable Long post_id) {
        CommentResponseDto result = commentService.createComment(post_id, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 댓글 수정
    @PutMapping("{post_id}/comments/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody CommentRequestDto requestDto, @PathVariable Long post_id) {
        try {
            CommentResponseDto result = commentService.updateComment(post_id, id, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 댓글 삭제
    @DeleteMapping("{post_id}/comments/{id}")
    public ResponseEntity<ApiResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @PathVariable Long post_id) {
        try {
            commentService.deleteComment(post_id, id, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}