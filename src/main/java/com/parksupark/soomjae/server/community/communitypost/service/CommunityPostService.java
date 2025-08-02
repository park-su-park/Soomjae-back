package com.parksupark.soomjae.server.community.communitypost.service;

import static com.parksupark.soomjae.server.common.exception.ErrorMessages.COMMUNITY_POST_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.common.constant.PostConstant.COMMUNITY_POST_TYPE;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.comment.repository.CommentRepository;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostDetailResponse;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostListResponse;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostRequest;
import com.parksupark.soomjae.server.community.communitypost.dto.CommunityPostResponse;
import com.parksupark.soomjae.server.community.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.community.communitypost.repository.CommunityPostRepository;
import com.parksupark.soomjae.server.community.location.constant.LocationConstant;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.community.location.repository.LocationRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long create(
        CommunityPostRequest communityPostRequest, UsernamePasswordUserDetails userDetails) {
        Member member = userDetails.getMember();

        Category category = null;
        if (communityPostRequest.getCategory() != null) {
            category = categoryRepository.findById(
                    Long.parseLong(communityPostRequest.getCategory()))
                .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
        }
        Location location = null;
        if (communityPostRequest.getLocation() != null) {
            location = locationRepository.findByCode(
                    Long.parseLong(communityPostRequest.getLocation()))
                .orElseThrow(() -> new IllegalStateException(
                    LocationConstant.LOCATION_NOT_FOUND));
        }
        CommunityPost entity = communityPostRequest.toEntity(member, category, location);

        return communityPostRepository.save(entity).getId();
    }

    public CommunityPostListResponse readByFilter(Pageable pageable) {
        List<CommunityPost> contents = communityPostRepository.findAll(pageable).getContent();
        List<CommunityPostResponse> response = new ArrayList<>();
        for (CommunityPost post : contents) {
            long commentNum = commentRepository.countByPostTypeAndPostIdAndDeletedTimeIsNull(
                COMMUNITY_POST_TYPE, post.getId());
            CommunityPostResponse communityPostResponse = CommunityPostResponse.of(post,
                commentNum);
            response.add(communityPostResponse);
        }
        return CommunityPostListResponse.of(response);
    }

    public CommunityPostDetailResponse readByPostId(Long postId) {
        CommunityPost communityPost = communityPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));
        List<CommentResponse> comments = commentRepository
            .findByPostTypeAndPostIdAndDeletedTimeIsNull(
                COMMUNITY_POST_TYPE, communityPost.getId()).stream().map(CommentResponse::of)
            .toList();
        return CommunityPostDetailResponse.of(communityPost, comments);
    }

    //페이징 기능 추 후 구현
    public CommunityPostListResponse readByMemberId(Long memberId, Pageable pageable) {
        List<CommunityPost> posts = communityPostRepository.findByMemberId(memberId, pageable)
            .getContent();
        List<CommunityPostResponse> response = new ArrayList<>();
        for (CommunityPost post : posts) {
            long commentNum = commentRepository.countByPostTypeAndPostIdAndDeletedTimeIsNull(
                COMMUNITY_POST_TYPE, post.getId());
            response.add(CommunityPostResponse.of(post, commentNum));
        }
        return CommunityPostListResponse.of(response);
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

        Category category = null;
        if (communityPostRequest.getCategory() != null) {
            category = categoryRepository.findById(
                    Long.parseLong(communityPostRequest.getCategory()))
                .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND));
        }
        Location location = null;
        if (communityPostRequest.getLocation() != null) {
            location = locationRepository.findByCode(
                    Long.parseLong(communityPostRequest.getLocation()))
                .orElseThrow(() -> new IllegalStateException(
                    LocationConstant.LOCATION_NOT_FOUND));
        }
        communityPost.setTitle(communityPostRequest.getTitle());
        communityPost.setContent(communityPostRequest.getContent());
        communityPost.setCategory(category);
        communityPost.setLocation(location);
    }

    @Transactional
    public void delete(Long postId) {
        CommunityPost communityPost = communityPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));
        communityPostRepository.delete(communityPost);
    }


}
