package com.spr.socialtv.service;

import com.spr.socialtv.dto.PostDto;
import com.spr.socialtv.dto.UserProfileDto;
import com.spr.socialtv.entity.Post;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 전체 가져오기
    @Transactional
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return convertToDtoList(posts);
    }

    // 특정 게시글 불러오기
    @Transactional
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return convertToDto(post);
        }
        return null;
    }

    // 게시글 생성 기능
    @Transactional
    public PostDto createPost(PostDto postDto, User user) {
        Post post = convertToPost(postDto, user);
        Post result = postRepository.save(post);
        return convertToDto(result);
    }

    // 게시글 수정 기능
    @Transactional
    public PostDto updatePost(Long postId, PostDto postDto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 POST_ID(" + postId + ") 를 찾을 수 없습니다."));
        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException("이 게시물을 업데이트할 권한이 없습니다."); // TODO : 에러 메시지 + 코드 변경
        }
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    // 게시글 삭제 기능
    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 POST_ID(\" + postId + \") 를 찾을 수 없습니다."));
        if (!post.getUser().equals(user)) {
            throw new IllegalArgumentException("이 게시물을 업데이트할 권한이 없습니다."); // TODO : 에러 메시지 + 코드 변경
        }
        postRepository.delete(post);
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
                .imageKey(post.getImageKey())
                .createDate(post.getCreateDate())
                .updateDate(post.getUpdateDate())
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
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .imageKey(dto.getImageKey())
                .user(user)
                .build();
        return build;
    }

    public List<PostDto> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return convertToDtoList(posts);
    }

    public PostDto getPostDetails(Long userId, Long postId) {
        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("해당 postId를 찾을 수 없습니다. : " + postId));
        return convertToDto(post);
    }
}