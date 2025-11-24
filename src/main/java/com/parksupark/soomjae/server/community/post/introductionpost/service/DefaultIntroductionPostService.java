package com.parksupark.soomjae.server.community.post.introductionpost.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.common.exception.ResourceOwnershipException;
import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.introductionpost.dto.IntroductionPostResponse;
import com.parksupark.soomjae.server.community.post.introductionpost.dto.UpdateIntroductionPostRequest;
import com.parksupark.soomjae.server.community.post.introductionpost.entity.IntroductionPost;
import com.parksupark.soomjae.server.community.post.introductionpost.repository.IntroductionPostRepository;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultIntroductionPostService implements IntroductionPostService {

    private final IntroductionPostRepository introductionPostRepository;

    @Override
    @Transactional(readOnly = true)
    public PostResponse readIntroductionPostById(Long postId) {
        IntroductionPost introductionPost = introductionPostRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        MemberResponse memberResponse = MemberResponse.create(introductionPost.getMember());

        return new IntroductionPostResponse(introductionPost.getId(), memberResponse,
            introductionPost.getContent(), introductionPost.getCreatedTime());
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse readIntroductionPostByMemberId(Long memberId) {
        IntroductionPost introductionPost = introductionPostRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        MemberResponse memberResponse = MemberResponse.create(introductionPost.getMember());

        return new IntroductionPostResponse(introductionPost.getId(), memberResponse,
            introductionPost.getContent(), introductionPost.getCreatedTime());
    }

    @Override
    @Transactional
    public PostResponse updateIntroductionPost(UpdateIntroductionPostRequest request,
        UsernamePasswordUserDetails userDetails) {

        Member member = userDetails.getMember();

        IntroductionPost introductionPost = introductionPostRepository.findByMemberId(
                member.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        introductionPost.updateIntroductionPost(request);

        return new IntroductionPostResponse(introductionPost.getId(),
            MemberResponse.create(member), introductionPost.getContent(),
            introductionPost.getCreatedTime());
    }

    @Override
    @Transactional
    public void deleteIntroductionPostById(Long postId, UsernamePasswordUserDetails userDetails) {
        Member member = userDetails.getMember();

        IntroductionPost introductionPost = introductionPostRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        if (!member.getId().equals(introductionPost.getMember().getId())) {
            throw new ResourceOwnershipException(ErrorMessages.POST_OWNER_MISMATCH_MESSAGE);
        }

        introductionPostRepository.delete(introductionPost);
    }

    @Override
    @Transactional
    public void deleteIntroductionPostByMemberId(Long memberId,
        UsernamePasswordUserDetails userDetails) {

        Member member = userDetails.getMember();

        IntroductionPost introductionPost = introductionPostRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        if (!member.getId().equals(introductionPost.getMember().getId())) {
            throw new ResourceOwnershipException(ErrorMessages.POST_OWNER_MISMATCH_MESSAGE);
        }

        introductionPostRepository.delete(introductionPost);
    }
}
