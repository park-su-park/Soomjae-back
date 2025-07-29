package com.parksupark.soomjae.server.community.comment.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.community.comment.dto.CommentListResponse;
import com.parksupark.soomjae.server.community.comment.dto.CommentRequest;
import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.comment.entity.Comment;
import com.parksupark.soomjae.server.community.comment.repository.CommentRepository;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostIdException;
import com.parksupark.soomjae.server.community.validator.PostValidator;
import com.parksupark.soomjae.server.community.validator.PostValidatorFactory;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final PostValidatorFactory postValidatorFactory;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse create(CommentRequest request, String postType, Long postId,
                                  UsernamePasswordUserDetails userDetails) {
        validatePost(postType, postId);

        Member member = userDetails.getMember();

        Comment comment = Comment.builder()
                .postId(postId)
                .postType(postType)
                .content(request.getContent())
                .member(member)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.of(savedComment);
    }

    public CommentListResponse readByPostTypeAndPostId(String postType, Long postId) {
        validatePost(postType, postId);

        List<Comment> byPostTypeAndPostId = commentRepository
                .findByPostTypeAndPostIdAndDeletedTimeIsNull(postType, postId);

        List<CommentResponse> commentResponseList = byPostTypeAndPostId.stream()
                .map(CommentResponse::of)
                .toList();

        return CommentListResponse.of(commentResponseList);
    }

    @Transactional
    public void delete(String postType, Long postId, Long commentId) {
        validatePost(postType, postId);
        Comment comment = commentRepository.findByIdAndDeletedTimeIsNull(commentId)
                .orElseThrow(() -> new IllegalStateException("해당 Id를 가진 comment가 존재하지 않습니다."));
        comment.markDeleted();
    }

    private void validatePost(String postType, Long postId) {
        PostValidator validator = postValidatorFactory.getValidator(postType);
        if (!validator.isValid(postId)) {
            throw new InvalidPostIdException(
                    ErrorMessages.INVALID_POST_ID_EXCEPTION_MESSAGE + postId);
        }
    }
}
