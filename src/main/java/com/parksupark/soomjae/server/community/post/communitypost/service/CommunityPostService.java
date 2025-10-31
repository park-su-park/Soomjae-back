package com.parksupark.soomjae.server.community.post.communitypost.service;

import static com.parksupark.soomjae.server.common.exception.ErrorMessages.COMMUNITY_POST_NOT_FOUND;
import static com.parksupark.soomjae.server.community.category.constant.CategoryConstant.CATEGORY_NOT_FOUND;
import static com.parksupark.soomjae.server.community.common.constant.PostConstant.COMMUNITY_POST_TYPE;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.category.entity.Category;
import com.parksupark.soomjae.server.community.category.repository.CategoryRepository;
import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.comment.repository.CommentRepository;
import com.parksupark.soomjae.server.community.like.repository.LikeRepository;
import com.parksupark.soomjae.server.community.location.constant.LocationConstant;
import com.parksupark.soomjae.server.community.location.entity.Location;
import com.parksupark.soomjae.server.community.location.repository.LocationRepository;
import com.parksupark.soomjae.server.community.post.common.dto.PostListResponse;
import com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostRequest;
import com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostResponse;
import com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostResponseWithComments;
import com.parksupark.soomjae.server.community.post.communitypost.dto.CommunityPostStatsResponse;
import com.parksupark.soomjae.server.community.post.communitypost.entity.CommunityPost;
import com.parksupark.soomjae.server.community.post.communitypost.repository.CommunityPostRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public Long create(
        CommunityPostRequest communityPostRequest, UsernamePasswordUserDetails userDetails) {

        Category category = getCategory(communityPostRequest);
        Location location = getLocation(communityPostRequest);
        Member member = userDetails.getMember();

        CommunityPost entity = communityPostRequest.toEntity(member, category, location);

        return communityPostRepository.save(entity).getId();
    }


    public PostListResponse readCommunityPostList(
        Pageable pageable,
        List<Long> categoryIds,
        List<Long> locationCodes,
        String keyword,
        UsernamePasswordUserDetails userDetails
    ) {
        List<Long> normalizedCategoryIds =
            (categoryIds == null || categoryIds.isEmpty()) ? null : categoryIds;
        List<Long> normalizedLocationIds =
            (locationCodes == null || locationCodes.isEmpty()) ? null : locationCodes;
        String normalizedKeyword =
            (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        Page<CommunityPost> filteredList = communityPostRepository
            .searchByCategoriesAndLocationsAndKeyword(
                normalizedCategoryIds, normalizedLocationIds, normalizedKeyword, pageable
            );

        List<CommunityPost> posts = filteredList.getContent();
        Long memberId = (userDetails != null) ? userDetails.getMember().getId() : null;
        List<CommunityPostResponse> response = getCommunityPostResponses(posts, memberId);
        return PostListResponse.of(response);
    }

    private List<CommunityPostResponse> getCommunityPostResponses(List<CommunityPost> contents,
        Long memberId) {
        List<CommunityPostStatsResponse> postStats = communityPostRepository.findPostStats(
            contents.stream().map(CommunityPost::getId).toList(), memberId);

        List<CommunityPostResponse> response = new ArrayList<>();

        for (CommunityPostStatsResponse postStat : postStats) {
            Optional<CommunityPost> postOptional = contents.stream()
                .filter(p -> postStat.getPostId().equals(p.getId())).findFirst();

            postOptional.ifPresent(
                meetingPost -> response.add(CommunityPostResponse.of(meetingPost, postStat)));
        }
        return response;
    }

    public PostListResponse readByMemberId(Long memberId, Pageable pageable) {
        List<CommunityPost> posts = communityPostRepository.findByMemberId(memberId, pageable)
            .getContent();
        List<CommunityPostResponse> response = getCommunityPostResponses(posts, memberId);
        return PostListResponse.of(response);
    }

    public CommunityPostResponseWithComments readByPostId(Long postId,
        UsernamePasswordUserDetails userDetails) {
        CommunityPost communityPost = communityPostRepository.findById(postId)
            .orElseThrow(() -> new IllegalStateException(COMMUNITY_POST_NOT_FOUND));

        List<CommentResponse> comments = commentRepository
            .findByPostTypeAndPostIdAndDeletedTimeIsNull(
                COMMUNITY_POST_TYPE, communityPost.getId()).stream().map(CommentResponse::of)
            .toList();

        Boolean isLikedByMe = likeRepository.existsByPostTypeAndPostIdAndMemberId(
            COMMUNITY_POST_TYPE, communityPost.getId(), userDetails.getMember().getId());

        Long likeNum = likeRepository.countByPostTypeAndPostId(COMMUNITY_POST_TYPE,
            communityPost.getId());

        return CommunityPostResponseWithComments.of(communityPost, likeNum, isLikedByMe, comments);
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
        Category category = getCategory(communityPostRequest);
        Location location = getLocation(communityPostRequest);
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

    private Category getCategory(CommunityPostRequest communityPostRequest) {
        return communityPostRequest.getCategory() != null ? categoryRepository.findById(
                Long.parseLong(communityPostRequest.getCategory()))
            .orElseThrow(() -> new IllegalStateException(CATEGORY_NOT_FOUND)) : null;
    }

    private Location getLocation(CommunityPostRequest communityPostRequest) {
        return communityPostRequest.getLocation() != null ? locationRepository.findByCode(
                Long.parseLong(communityPostRequest.getLocation()))
            .orElseThrow(() -> new IllegalStateException(LocationConstant.LOCATION_NOT_FOUND))
            : null;
    }

}
