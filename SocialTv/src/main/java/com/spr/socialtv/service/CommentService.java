package com.spr.socialtv.service;


import com.spr.socialtv.dto.ApiResponseDto;
import com.spr.socialtv.dto.CommentRequestDto;
import com.spr.socialtv.dto.CommentResponseDto;
import com.spr.socialtv.entity.Comment;
import com.spr.socialtv.entity.Post;
import com.spr.socialtv.entity.User;
import com.spr.socialtv.entity.UserRoleEnum;
import com.spr.socialtv.repository.CommentRepository;
import com.spr.socialtv.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        Post post = findPost(postId);

        // 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
        // 댓글 저장
        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getComment());
        comment.setUser(user);
        comment.setPost(post);

        commentRepository.save(comment);

        return CommentResponseDto.builder()
                .postId(post.getPostId())
                .commentId(comment.getCommId())
                .comment(comment.getComment())
                .username(user.getUsername())
                .build();
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto commentRequestDto, User user) {
        Post post = findPost(postId);

        // 선택한 댓글의 DB 저장 유무를 확인하기
        Comment comment = findComment(commentId);
        // 관리자 기능 추가할것 0703 김태훈
        /*        if (!user.getUsername().equals(comment.getUser().getUsername())) {
            throw new IllegalArgumentException("작성자만 수정/삭제할 수 있습니다.");
        }*/

        if (this.checkValidUser(user, comment)) {
            // 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
            comment.setContent(commentRequestDto.getComment());
            commentRepository.save(comment);

            return CommentResponseDto.builder()
                    .postId(post.getPostId())
                    .commentId(comment.getCommId())
                    .username(comment.getUser().getUsername())
                    .comment(comment.getComment())
                    .build();

        } else {
            throw new IllegalArgumentException("작성자, 관리자 만 수정/삭제할 수 있습니다.");
        }
    }

    // 댓글 삭제
    @Transactional
    public ApiResponseDto deleteComment(Long postId, Long commentId, User user) {
        Post post = findPost(postId);

        // 선택한 댓글의 DB 저장 유무를 확인하기
        Comment comment = findComment(commentId);

        if (!user.getUsername().equals(comment.getUser().getUsername())) {
            throw new IllegalArgumentException("작성자만 수정/삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);

        return ApiResponseDto.builder()
                .msg("success")
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    // 해당 댓글이 DB에 존재하는지 확인
    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
    }

    // 선택한 게시글의 DB 저장 유무를 확인하기
    private Post findPost(Long postId) {
       return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }

    /**
     * 유효한 등록자인지 확인
     */
    private boolean checkValidUser(User user, Comment comment) {
        return !(
                !user.getUsername().equals(comment.getUser().getUsername())
                        && !user.getRole().equals(UserRoleEnum.ADMIN)
        );
    }
}
