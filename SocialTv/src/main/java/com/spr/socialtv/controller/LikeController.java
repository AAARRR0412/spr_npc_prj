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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post/Like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> insert(@RequestBody @Valid LikeRequestDTO likeRequestDTO) throws Exception {
        likeService.insert(likeRequestDTO);
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDto> delete(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        likeService.delete(likeRequestDTO);
        return ResponseEntity.ok().body(new ApiResponseDto("성공", HttpStatus.OK.value()));
    }
}
