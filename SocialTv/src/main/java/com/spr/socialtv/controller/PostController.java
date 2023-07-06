package com.spr.socialtv.controller;

import com.spr.socialtv.dto.PostDto;
import com.spr.socialtv.dto.UserProfileDto;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.jwt.JwtUtil;
import com.spr.socialtv.security.UserDetailsImpl;
import com.spr.socialtv.service.FileUploadService;
import com.spr.socialtv.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    private final FileUploadService fileUploadService;

    @Autowired
    public PostController(PostService postService, JwtUtil jwtUtil, FileUploadService fileUploadService) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
        this.fileUploadService = fileUploadService;
    }
    
    // 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    // 프로필 조회
    @GetMapping("/{postId}/user-profile")
    public ResponseEntity<UserProfileDto> getUserProfileByPostId(@PathVariable Long postId) {
        UserProfileDto userProfileDto = postService.getUserProfileByPostId(postId);
        if (userProfileDto != null) {
            return ResponseEntity.ok(userProfileDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 만들기
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(@RequestPart("postDto") PostDto postDto, @RequestPart("file") MultipartFile file, HttpServletRequest request) {

        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 사용자 정보 확인
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        if (file != null && !file.isEmpty()) {
            try {
                String imageKey = fileUploadService.uploadFile(file);
                postDto.setImageKey(imageKey);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        PostDto resultDto = postService.createPost(postDto, user);
        return new ResponseEntity<>(resultDto, HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto, HttpServletRequest request) {

        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 사용자 정보 확인
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        PostDto updatedPostDto = postService.updatePost(postId, postDto, user);
        return new ResponseEntity<>(updatedPostDto, HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 사용자 정보 확인

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        postService.deletePost(postId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}