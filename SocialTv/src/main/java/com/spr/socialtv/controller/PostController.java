package com.spr.socialtv.controller;

import com.spr.socialtv.dto.PostDto;
import com.spr.socialtv.dto.UserProfileDto;
import com.spr.socialtv.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
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

    /*
     * 등록
     * */
    @PostMapping("/post")
    public ResponseEntity<PostDto> write(@RequestBody @Valid PostDto postDto, HttpServletRequest request) {
        ResponseEntity<PostDto> result = postService.savePost(postDto, request);
        return result;
    }

}