package com.parksupark.soomjae.server.community.post.memberpost.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.common.exception.ResourceOwnershipException;
import com.parksupark.soomjae.server.community.comment.dto.CommentResponse;
import com.parksupark.soomjae.server.community.comment.entity.Comment;
import com.parksupark.soomjae.server.community.comment.repository.CommentRepository;
import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.like.dto.LikeStatusResponse;
import com.parksupark.soomjae.server.community.like.service.LikeService;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostDetailResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostFeedResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostGridProjection;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostIdResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.SaveMemberPostRequest;
import com.parksupark.soomjae.server.community.post.memberpost.entity.MemberPost;
import com.parksupark.soomjae.server.community.post.memberpost.entity.MemberPostImage;
import com.parksupark.soomjae.server.community.post.memberpost.repository.MemberPostRepository;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.exception.MemberNotFoundException;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultMemberPostService implements MemberPostService {

    private final MemberRepository memberRepository;
    private final MemberPostRepository memberPostRepository;
    private final LikeService likeService;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public MemberPostIdResponse createMemberPost(SaveMemberPostRequest request,
        Member member) {

        MemberPost newPost = MemberPost.create(member, request.getContent());

        request.getImageUrls().forEach(imageUrl -> {
            MemberPostImage image = MemberPostImage.create(imageUrl);
            newPost.addImage(image);
        });

        MemberPost savedPost = memberPostRepository.save(newPost);

        return new MemberPostIdResponse(savedPost.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberPostDetailResponse readMemberPost(Long memberPostId,
        @Nullable UsernamePasswordUserDetails userDetails) {

        MemberPost memberPost = memberPostRepository.findByIdWithMember(memberPostId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.MEMBER_POST_NOT_FOUND));

        LikeStatusResponse likeStatusResponse = likeService.readLikeStatus(
            PostConstant.MEMBER_POST_TYPE,
            memberPostId, userDetails);

        List<Comment> comments = commentRepository.findByPostTypeAndPostIdAndDeletedTimeIsNull(
            PostConstant.MEMBER_POST_TYPE,
            memberPostId);

        List<CommentResponse> commentResponses = comments.stream().map(CommentResponse::of)
            .collect(Collectors.toList());

        return new MemberPostDetailResponse(memberPost, memberPost.getMember(), likeStatusResponse,
            (long) comments.size(), commentResponses);
    }

    @Override
    @Transactional
    public Page<MemberPostFeedResponse> readFeedMemberPosts(Pageable pageable,
        @Nullable UsernamePasswordUserDetails userDetails) {

        Page<MemberPost> feedsPage = memberPostRepository.findAllByOrderByCreatedTimeDesc(
            pageable);

        return feedsPage.map((post) -> {

            LikeStatusResponse likeStatusResponse = likeService.readLikeStatus(
                PostConstant.MEMBER_POST_TYPE,
                post.getId(), userDetails);

            long commentCount = commentRepository.countByPostTypeAndPostIdAndDeletedTimeIsNull(
                PostConstant.MEMBER_POST_TYPE, post.getId());

            return new MemberPostFeedResponse(post, post.getMember(), likeStatusResponse,
                commentCount);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberPostGridProjection> readGridMemberPosts(Long memberId, Pageable pageable) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(ErrorMessages.MEMBER_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        return memberPostRepository.findMemberPostGridByMemberId(
            memberId, pageable);
    }

    @Override
    @Transactional
    public MemberPostIdResponse updateMemberPost(SaveMemberPostRequest request, Long memberPostId,
        Member member) {
        MemberPost memberPost = memberPostRepository.findByIdWithMember(memberPostId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.MEMBER_POST_NOT_FOUND));

        if (!memberPost.getMember().getId().equals(member.getId())) {
            throw new ResourceOwnershipException(ErrorMessages.POST_OWNER_MISMATCH_MESSAGE);
        }

        memberPost.update(request.getContent(), request.getImageUrls());

        return new MemberPostIdResponse(memberPost.getId());
    }

    @Override
    @Transactional
    public void deleteMemberPost(Long memberPostId, Member member) {

        MemberPost memberPost = memberPostRepository.findByIdWithMember(memberPostId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.MEMBER_POST_NOT_FOUND));

        if (!memberPost.getMember().getId().equals(member.getId())) {
            throw new ResourceOwnershipException(ErrorMessages.POST_OWNER_MISMATCH_MESSAGE);
        }

        memberPostRepository.delete(memberPost);
    }
}
