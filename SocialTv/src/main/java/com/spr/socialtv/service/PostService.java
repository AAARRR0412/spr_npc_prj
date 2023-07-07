package com.spr.socialtv.service;

import com.spr.socialtv.dto.PostDto;
import com.spr.socialtv.dto.PostResponseDto;
import com.spr.socialtv.dto.UserProfileDto;
import com.spr.socialtv.entity.Post;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;

    public PostService(PostRepository postRepository, FileUploadService fileUploadService) {
        this.postRepository = postRepository;
        this.fileUploadService = fileUploadService;
    }

    // 게시글 전체 가져오기
    @Transactional
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return convertToDtoList(posts);
    }

    // 특정 게시글 불러오기
    @Transactional
    public PostResponseDto getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            String imageUrl = "https://news0412.s3.ap-northeast-2.amazonaws.com/" + post.getImageKey();
            return convertToPostResponseDto(post);
        }
        return null;
    }

    // 게시글 생성 기능
    @Transactional
    public PostDto createPost(PostDto postDto, User user) {
        // 최초 좋아요, 조회수는 0
        postDto.setLikeCount(0);
        postDto.setViewCount(0);
        Post post = convertToPost(postDto, user);
        Post result = postRepository.save(post);
        return convertToDto(result);
    }

    // 게시글 수정
    @Transactional
    public PostDto updatePost(Long postId, PostDto postDto, MultipartFile file, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 POST_ID(\" + postId + \") 를 찾을 수 없습니다."));
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("이 게시물을 업데이트할 권한이 없습니다."); // TODO : 에러 메시지 + 코드 변경
        }
        // 새로운 파일이 업로드되었다면
        if (file != null && !file.isEmpty()) {
            // 기존의 파일이 있으면 삭제
            if (post.getImageKey() != null && !post.getImageKey().isEmpty()) {
                fileUploadService.deleteFile(post.getImageKey());
            }
            // 새로운 파일 업로드
            String imageKey;
            try {
                imageKey = fileUploadService.uploadFile(file);
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not upload file", e);
            }
            post.setImageKey(imageKey);
        }
        // Dto의 변경사항을 엔티티에 맵핑
        post.setContent(postDto.getContent()); // content 필드 맵핑 추가
        post.setTitle(postDto.getTitle()); // title 필드 맵핑 추가
        post.setUser(user);
        // 엔티티 저장
        Post updatedPost = postRepository.save(post);
        // 업데이트된 엔티티를 Dto로 변환 후 리턴
        return convertToDto(updatedPost);
    }


    // 게시글 삭제 기능
    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 POST_ID(\" + postId + \") 를 찾을 수 없습니다."));
        if (!post.getUser().getId().equals(user.getId())) {
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

    private PostResponseDto convertToPostResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreateDate())
                .modifiedAt(post.getUpdateDate())
                .commentList(post.getComments())
                .username(post.getUser().getUsername())
                .userProfile(post.getUser().getSelfText())
                .imageUrl("https://news0412.s3.ap-northeast-2.amazonaws.com/" + post.getImageKey())
                .profileImageUrl("https://news0412.s3.ap-northeast-2.amazonaws.com/" + post.getUser().getProfileImageKey())
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
                .likeCount(dto.getLikeCount())
                .viewCount(dto.getViewCount())
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