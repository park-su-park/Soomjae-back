package com.parksupark.soomjae.server.community.communitypost.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostListResponse;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostRequest;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostResponse;
import com.parksupark.soomjae.server.community.communitypost.service.CommunityPostService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    @PostMapping("/v1/boards/community/posts")
    public ResponseEntity<Map<String, Object>> postCommunityPost(
            @RequestBody CommunityPostRequest communityPostRequest,
            @AuthenticationPrincipal
            UsernamePasswordUserDetails userDetails) {
        Long postId = communityPostService.create(communityPostRequest, userDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("postType", "community");

        return ResponseEntity.status(200).body(response);
    }


    //유저 마이페이지 위한 memberId로 다건 조회
    @GetMapping("/v1/members/{memberId}/activities/posts/community")
    ResponseEntity<CommunityPostListResponse> getByMemberId(@PathVariable Long memberId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(200)
                .body(communityPostService.readByMemberId(memberId, pageable));
    }


    //postId로 상세 조회
    @GetMapping("/v1/boards/community/posts/{postId}")
    ResponseEntity<CommunityPostResponse> getByPostId(@PathVariable Long postId) {
        return ResponseEntity.status(200).body(communityPostService.readBypostId(postId));
    }

    //리스트 조회
    @GetMapping("/v1/boards/community/posts/list")
    ResponseEntity<CommunityPostListResponse> getCommunityList(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(200).body(communityPostService.readByFilter(pageable));
    }

    //수정
    @PutMapping("/v1/boards/community/posts/{postId}")
    ResponseEntity<Long> putCommunityPost(@PathVariable Long postId,
            @RequestBody CommunityPostRequest request) {
        return ResponseEntity.status(200)
                .body(communityPostService.update(postId, request));
    }

    //삭제
    @DeleteMapping("/v1/boards/community/posts/{postId}")
    ResponseEntity<Void> deleteCommunityPost(@PathVariable Long postId) {
        communityPostService.delete(postId);
        return ResponseEntity.ok().build();
    }

}
