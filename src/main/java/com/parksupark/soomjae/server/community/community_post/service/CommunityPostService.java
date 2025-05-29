package com.parksupark.soomjae.server.community.community_post.service;

import static com.parksupark.soomjae.server.common.exception.ErrorMessages.COMMUNITY_POST_NOT_FOUND;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.community.community_post.dto.CommunityPostListResponse;
import com.parksupark.soomjae.server.community.community_post.dto.CommunityPostRequest;
import com.parksupark.soomjae.server.community.community_post.dto.CommunityPostResponse;
import com.parksupark.soomjae.server.community.community_post.entity.CommunityPost;
import com.parksupark.soomjae.server.community.community_post.repository.CommunityPostRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;

    @Transactional
    public Long create(CommunityPostRequest communityPostRequest, UsernamePasswordUserDetails userDetails) {
        Member member = userDetails.getMember();
        CommunityPost entity = communityPostRequest.toEntity(member);
        return communityPostRepository.save(entity).getId();
    }

    public CommunityPostListResponse readByFilter() {
        List<CommunityPostResponse> communityPostResponseList = communityPostRepository.findAll().stream()
            .map(CommunityPostResponse::of).toList();
        return new CommunityPostListResponse(communityPostResponseList);
    }

    public CommunityPostResponse readBypostId(Long postId) {
        CommunityPost communityPost = communityPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));
        return CommunityPostResponse.of(communityPost);
    }

    //페이징 기능 추 후 구현
    public CommunityPostListResponse readByMemberId(Long memberId) {
        List<CommunityPostResponse> posts = new ArrayList<>();
        communityPostRepository.findByMemberId(memberId).forEach(p -> posts.add(CommunityPostResponse.of(p)));
        return new CommunityPostListResponse(posts);
    }

    @Transactional
    public Long update(Long communityPostId, CommunityPostRequest communityPostRequest) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId)
            .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));
        updateCommunityPost(communityPostRequest, communityPost);
        return communityPost.getId();
    }

    private void updateCommunityPost(CommunityPostRequest communityPostRequest,
        CommunityPost communityPost) {
        communityPost.setTitle(communityPostRequest.getTitle());
        communityPost.setContent(communityPostRequest.getContent());
    }

    @Transactional
    public void delete(Long postId) {
        CommunityPost communityPost = communityPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));
        communityPostRepository.delete(communityPost);
    }


}
