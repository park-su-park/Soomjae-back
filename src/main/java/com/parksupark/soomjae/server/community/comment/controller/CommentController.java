package com.parksupark.soomjae.server.community.comment.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.comment.dto.CommentListResponse;
import com.parksupark.soomjae.server.community.comment.dto.CommentRequest;
import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> postComment(@RequestBody CommentRequest request,
                                                       @PathVariable("boardType") String postType,
                                                       @PathVariable Long postId,
                                                       @AuthenticationPrincipal
                                                       UsernamePasswordUserDetails userDetails) {

        return ResponseEntity.ok(commentService.create(request, postType, postId, userDetails));
    }

    @GetMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<CommentListResponse> getByPostTypeAndPostId(
            @PathVariable("boardType") String postType, @PathVariable Long postId) {
        return ResponseEntity.ok(
                commentService.readByPostTypeAndPostId(postType, postId));
    }

    @DeleteMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("boardType") String postType,
                                              @PathVariable Long postId,
                                              @PathVariable Long commentId) {
        commentService.delete(postType, postId, commentId);
        return ResponseEntity.ok(null);
    }
}
