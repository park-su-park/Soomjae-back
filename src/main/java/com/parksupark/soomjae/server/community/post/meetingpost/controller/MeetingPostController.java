package com.parksupark.soomjae.server.community.post.meetingpost.controller;

import static com.parksupark.soomjae.server.community.common.constant.PostConstant.MEETING_POST_TYPE;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.participation.dto.ParticipantListResponse;
import com.parksupark.soomjae.server.community.participation.dto.ParticipationResponse;
import com.parksupark.soomjae.server.community.post.common.dto.PostListResponse;
import com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostRequest;
import com.parksupark.soomjae.server.community.post.meetingpost.dto.MeetingPostResponseWithComments;
import com.parksupark.soomjae.server.community.post.meetingpost.service.MeetingPostService;
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
public class MeetingPostController {

    private final MeetingPostService meetingPostService;

    @PostMapping("/v1/boards/meeting/posts")
    public ResponseEntity<Map<String, Object>> postMeetingPost(
        @RequestBody MeetingPostRequest postRequest,
        @AuthenticationPrincipal
        UsernamePasswordUserDetails userDetails) {
        Long postId = meetingPostService.create(postRequest, userDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("postId", postId);
        response.put("postType", MEETING_POST_TYPE);

        return ResponseEntity.ok(response);
    }


    //유저 마이페이지 위한 memberId로 다건 조회
    @GetMapping("/v1/members/{memberId}/activities/posts/meeting")
    ResponseEntity<PostListResponse> getByMemberId(@PathVariable Long memberId,
        @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Pageable zeroBasedPageable = Pageable.ofSize(pageable.getPageSize())
            .withPage(Math.max(pageable.getPageNumber() - 1, 0));
        return ResponseEntity.ok(meetingPostService.readByMemberId(memberId, zeroBasedPageable));
    }


    //postId로 상세 조회
    @GetMapping("/v1/boards/meeting/posts/{postId}")
    ResponseEntity<MeetingPostResponseWithComments> getByPostId(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {
        return ResponseEntity.ok(meetingPostService.readByPostId(postId, userDetails));
    }

    //리스트 조회
    @GetMapping("/v1/boards/meeting/posts/list")
    ResponseEntity<PostListResponse> getMeetingPostList(
        @PageableDefault(size = 10, page = 0) Pageable pageable,
        @RequestParam(value = "categoryId", required = false) List<Long> categoryIds,
        @RequestParam(value = "locationCodes", required = false) List<Long> locationCodes,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "recruitment", required = false) Boolean recruitment,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {
        Pageable zeroBasedPageable = Pageable.ofSize(pageable.getPageSize())
            .withPage(Math.max(pageable.getPageNumber() - 1, 0));
        return ResponseEntity.ok(
            meetingPostService.readMeetingPostList(zeroBasedPageable, categoryIds, locationCodes,
                keyword, recruitment, userDetails));
    }

    //수정
    @PutMapping("/v1/boards/meeting/posts/{postId}")
    ResponseEntity<Long> putMeetingPost(@PathVariable Long postId,
        @RequestBody MeetingPostRequest request) {
        return ResponseEntity.ok(meetingPostService.update(postId, request));
    }

    //삭제
    @DeleteMapping("/v1/boards/meeting/posts/{postId}")
    ResponseEntity<Void> deleteMeetingPost(@PathVariable Long postId) {
        meetingPostService.delete(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/boards/meeting/posts/{postId}/join")
    ResponseEntity<ParticipationResponse> createParticipation(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {
        return ResponseEntity.ok(meetingPostService.participate(postId, userDetails));
    }

    @DeleteMapping("/v1/boards/meeting/posts/{postId}/join")
    ResponseEntity<String> deleteParticipation(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {
        return ResponseEntity.ok(meetingPostService.cancelParticipation(postId, userDetails));
    }

    @GetMapping("/v1/boards/meeting/posts/{postId}/participants")
    ResponseEntity<ParticipantListResponse> readParticipants(@PathVariable Long postId) {
        return ResponseEntity.ok(meetingPostService.findAllParticipantsByPostId(postId));
    }

    @PutMapping("/v1/boards/meeting/post/{postId}/end")
    ResponseEntity<Void> endMeeting(@PathVariable Long postId,
        @AuthenticationPrincipal UsernamePasswordUserDetails userDetails) {
        meetingPostService.endMeeting(postId, userDetails);
        return ResponseEntity.ok().build();
    }
}
