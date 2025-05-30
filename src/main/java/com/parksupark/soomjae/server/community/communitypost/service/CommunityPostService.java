package com.parksupark.soomjae.server.community.communitypost.service;

import static com.parksupark.soomjae.server.common.exception.ErrorMessages.COMMUNITY_POST_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NOT_FOUND;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostListResponse;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostRequest;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostResponse;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.community.communitypost.repository.CommunityPostRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long create(
            CommunityPostRequest communityPostRequest, UsernamePasswordUserDetails userDetails) {
        Member member = userDetails.getMember();
        Category category = categoryRepository.findByName(communityPostRequest.getCategory())
                .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
        CommunityPost entity = communityPostRequest.toEntity(member, category);
        return communityPostRepository.save(entity).getId();
    }

    public CommunityPostListResponse readByFilter(Pageable pageable) {
        Page<CommunityPost> postPage = communityPostRepository.findAll(pageable);
        List<CommunityPostResponse> postResponseList = postPage.map(CommunityPostResponse::of)
                .getContent();
        return new CommunityPostListResponse(postResponseList);
    }


    public CommunityPostResponse readBypostId(Long postId) {
        CommunityPost communityPost = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));
        return CommunityPostResponse.of(communityPost);
    }

    //페이징 기능 추 후 구현
    public CommunityPostListResponse readByMemberId(Long memberId, Pageable pageable) {
        Page<CommunityPost> postPage = communityPostRepository.findByMemberId(memberId, pageable);
        List<CommunityPostResponse> postResponseList = postPage.map(CommunityPostResponse::of)
                .getContent();
        return new CommunityPostListResponse(postResponseList);
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
        Category category = categoryRepository.findByName(communityPostRequest.getCategory())
                .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
        communityPost.setTitle(communityPostRequest.getTitle());
        communityPost.setContent(communityPostRequest.getContent());
        communityPost.setCategory(category);
    }

    @Transactional
    public void delete(Long postId) {
        CommunityPost communityPost = communityPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));
        communityPostRepository.delete(communityPost);
    }


}
