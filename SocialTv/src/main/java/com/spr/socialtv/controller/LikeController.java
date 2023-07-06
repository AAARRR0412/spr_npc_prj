package com.spr.socialtv.controller;

import com.spr.socialtv.dto.ApiResponseDto;
import com.spr.socialtv.dto.LikeRequestDTO;
import com.spr.socialtv.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 좋아요
 * */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/like")
public class LikeController {
    private final LikeService likeService;

    // 좋아요
    @PostMapping
    public ResponseEntity<ApiResponseDto> insert(@RequestBody @Valid LikeRequestDTO likeRequestDTO) throws Exception {
        ResponseEntity<ApiResponseDto> result = likeService.insert(likeRequestDTO);
        return result;
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<ApiResponseDto> delete(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        likeService.delete(likeRequestDTO);
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }
}
