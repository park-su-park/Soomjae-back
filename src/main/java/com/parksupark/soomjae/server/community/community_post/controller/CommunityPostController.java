package com.parksupark.soomjae.server.community.community_post.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.community_post.dto.CommunityPostListResponse;
import com.parksupark.soomjae.server.community.community_post.dto.CommunityPostRequest;
import com.parksupark.soomjae.server.community.community_post.dto.CommunityPostResponse;
import com.parksupark.soomjae.server.community.community_post.service.CommunityPostService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/boards/community/posts")
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> postCommunityPost(@RequestBody CommunityPostRequest communityPostRequest,@AuthenticationPrincipal
    UsernamePasswordUserDetails userDetails) {
        Long postId = communityPostService.create(communityPostRequest, userDetails);
        System.out.println(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("postType", "community");

        return ResponseEntity.status(200).body(response);
    }

    ResponseEntity<CommunityPostListResponse> getByMemberId(Long memberId) {
        return ResponseEntity.status(200).body(communityPostService.readByMemberId(memberId));
    }

    ResponseEntity<CommunityPostResponse> getByPostId(Long postId) {
        return ResponseEntity.status(200).body(communityPostService.readBypostId(postId));
    }

    ResponseEntity<Long> putCommunityPost(Long communityPostId,
        CommunityPostRequest request) {
        return ResponseEntity.status(200)
            .body(communityPostService.update(communityPostId, request));
    }

    ResponseEntity<Void> deleteCommunityPost(Long communityPostId, @AuthenticationPrincipal UserDetails userDetail) {
        communityPostService.delete(communityPostId);
        return ResponseEntity.status(200).body(null);
    }

}
