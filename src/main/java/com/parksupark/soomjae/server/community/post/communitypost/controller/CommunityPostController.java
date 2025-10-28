package com.parksupark.soomjae.server.community.post.communitypost.controller;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.common.dto.PostListResponse;
import com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostRequest;
import com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostResponseWithComments;
import com.parksupark.soomjae.server.community.post.communitypost.service.CommunityPostService;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
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

        return ResponseEntity.ok(response);
    }


    //유저 마이페이지 위한 memberId로 다건 조회
    @GetMapping("/v1/members/{memberId}/activities/posts/community")
    ResponseEntity<PostListResponse> getByMemberId(@PathVariable Long memberId,
        @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Pageable zeroBasedPageable = Pageable.ofSize(pageable.getPageSize())
            .withPage(Math.max(pageable.getPageNumber() - 1, 0));
        return ResponseEntity.ok(communityPostService.readByMemberId(memberId, zeroBasedPageable));
    }


    //postId로 상세 조회
    @GetMapping("/v1/boards/community/posts/{postId}")
    ResponseEntity<CommunityPostResponseWithComments> getByPostId(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {
        return ResponseEntity.ok(communityPostService.readByPostId(postId, userDetails));
    }

    //리스트 조회
    @GetMapping("/v1/boards/community/posts/list")
    ResponseEntity<PostListResponse> getCommunityList(
        @PageableDefault(size = 10, page = 0) Pageable pageable,
        @RequestParam(value = "categoryId", required = false) List<Long> categoryIds,
        @RequestParam(value = "locationId", required = false) List<Long> locationIds,
        @RequestParam(value = "keyword", required = false) String keyword,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails
    ) {
        Pageable zeroBasedPageable = Pageable.ofSize(pageable.getPageSize())
            .withPage(Math.max(pageable.getPageNumber() - 1, 0));

        return ResponseEntity.ok(
            communityPostService.readCommunityPostList(
                zeroBasedPageable, categoryIds, locationIds, keyword, userDetails
            )
        );
    }

    //수정
    @PutMapping("/v1/boards/community/posts/{postId}")
    ResponseEntity<Long> putCommunityPost(@PathVariable Long postId,
        @RequestBody CommunityPostRequest request) {
        return ResponseEntity.ok(communityPostService.update(postId, request));
    }

    //삭제
    @DeleteMapping("/v1/boards/community/posts/{postId}")
    ResponseEntity<Void> deleteCommunityPost(@PathVariable Long postId) {
        communityPostService.delete(postId);
        return ResponseEntity.ok().build();
    }

}
