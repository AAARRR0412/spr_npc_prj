package com.spr.socialtv.service;

import com.spr.socialtv.dto.PostDto;
import com.spr.socialtv.dto.UserProfileDto;
import com.spr.socialtv.entity.Post;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.jwt.JwtUtil;
import com.spr.socialtv.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final JwtUtil jwtUtil;

    public PostService(PostRepository postRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return convertToDtoList(posts);
    }

    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return convertToDto(post);
        }
        return null;
    }


    //region

    private List<PostDto> convertToDtoList(List<Post> posts) {
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writerName(post.getUser().getUsername())
                .build();
    }


    public UserProfileDto getUserProfileByPostId(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            User user = post.getUser();
            return convertToUserProfileDto(user);
        }
        return null;
    }

    private UserProfileDto convertToUserProfileDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private Post convertToPost(PostDto dto, User user) {
        Post build = Post.builder()
                //.author(author)
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
        return build;
    }


    //endregion

    public List<PostDto> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return convertToDtoList(posts);
    }

    public PostDto getPostDetails(Long userId, Long postId) {
        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("해당 postId를 찾을 수 없습니다. : " + postId));
        return convertToDto(post);
    }



    /*
     * 등록
     * */
    @Transactional
    public ResponseEntity<PostDto> savePost(PostDto postDto, HttpServletRequest request) {
        /*
         * 토큰 검증.
         */
        User user = jwtUtil.checkToken(request);
        Post result = postRepository.save(convertToPost(postDto, user));
        PostDto resultDto = convertToDto(result);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (result.getId() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } else {
            return new ResponseEntity<>(resultDto, HttpStatus.OK);
        }
    }



}