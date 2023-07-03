package com.spr.socialtv.controller;

import com.spr.socialtv.dto.*;
import com.spr.socialtv.service.PostService;
import com.spr.socialtv.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController  // 주소가 아닌 문자열로 그대로 리턴 가능함
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PostService postService;

    /*
     * 회원 가입
     * */
    @PostMapping("/signup")
    @ResponseBody
    public HashMap<String, Object> signup(@RequestBody @Valid SignupRequestDto signupRequestDto){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map =  userService.signup(signupRequestDto);

        return map;
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        UserProfileDto userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<PostDto>> getUserPosts(@PathVariable Long userId) {
        List<PostDto> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{userId}/posts/{postId}")
    public ResponseEntity<PostDto> getPostDetails(@PathVariable Long userId, @PathVariable Long postId) {
        PostDto postDetails = postService.getPostDetails(userId, postId);
        return ResponseEntity.ok(postDetails);
    }

    @PutMapping("/users/{userId}/profile")
    public ResponseEntity<UserProfileDto> updateUserProfile(@PathVariable Long userId, @RequestBody UserProfileRequestDto requestDto) {
        UserProfileDto updatedProfile = userService.updateUserProfile(userId, requestDto);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
