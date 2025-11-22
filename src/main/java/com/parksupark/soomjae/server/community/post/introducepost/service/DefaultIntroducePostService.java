package com.parksupark.soomjae.server.community.post.introducepost.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.common.exception.ResourceAlreadyExistsException;
import com.parksupark.soomjae.server.common.exception.ResourceNotFoundException;
import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.introducepost.dto.CreateIntroducePostRequest;
import com.parksupark.soomjae.server.community.post.introducepost.dto.IntroducePostResponse;
import com.parksupark.soomjae.server.community.post.introducepost.dto.UpdateIntroducePostRequest;
import com.parksupark.soomjae.server.community.post.introducepost.entity.IntroducePost;
import com.parksupark.soomjae.server.community.post.introducepost.repository.IntroducePostRepository;
import com.parksupark.soomjae.server.member.dto.MemberResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultIntroducePostService implements IntroducePostService {

    private final IntroducePostRepository introducePostRepository;

    @Override
    @Transactional
    public Long createIntroducePost(CreateIntroducePostRequest request,
        UsernamePasswordUserDetails userDetails) {

        Member member = userDetails.getMember();
        if (introducePostRepository.existsByMember(member)) {
            throw new ResourceAlreadyExistsException(
                ErrorMessages.INTRODUCE_POST_ALREADY_EXISTS_MESSAGE);
        }

        IntroducePost introducePost = IntroducePost.create(request);
        introducePost.setMember(member);

        IntroducePost save = introducePostRepository.save(introducePost);
        return save.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse readIntroducePostById(Long postId) {
        IntroducePost introducePost = introducePostRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        MemberResponse memberResponse = MemberResponse.create(introducePost.getMember());

        return new IntroducePostResponse(introducePost.getId(), memberResponse,
            introducePost.getContent(), introducePost.getCreatedTime());
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse readIntroducePostByMemberId(Long memberId) {
        IntroducePost introducePost = introducePostRepository.findByMemberId(memberId).orElseThrow(
            () -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        MemberResponse memberResponse = MemberResponse.create(introducePost.getMember());

        return new IntroducePostResponse(introducePost.getId(), memberResponse,
            introducePost.getContent(), introducePost.getCreatedTime());
    }

    @Override
    @Transactional
    public PostResponse updateIntroducePost(UpdateIntroducePostRequest request,
        UsernamePasswordUserDetails userDetails) {

        Member member = userDetails.getMember();

        IntroducePost introducePost = introducePostRepository.findByMemberId(member.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        introducePost.updateIntroducePost(request);

        return new IntroducePostResponse(introducePost.getId(), MemberResponse.create(member),
            introducePost.getContent(), introducePost.getCreatedTime());

    }

    @Override
    @Transactional
    public void deleteIntroducePostById(Long postId, UsernamePasswordUserDetails userDetails) {
        Member member = userDetails.getMember();

        IntroducePost introducePost = introducePostRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        if (!member.getId().equals(introducePost.getMember().getId())) {
            throw new ResourceNotFoundException(ErrorMessages.POST_OWNER_MISMATCH_MESSAGE);
        }

        introducePostRepository.delete(introducePost);
    }

    @Override
    @Transactional
    public void deleteIntroducePostByMemberId(Long memberId,
        UsernamePasswordUserDetails userDetails) {

        Member member = userDetails.getMember();

        IntroducePost introducePost = introducePostRepository.findByMemberId(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND));

        if (!member.getId().equals(introducePost.getMember().getId())) {
            throw new ResourceNotFoundException(ErrorMessages.POST_OWNER_MISMATCH_MESSAGE);
        }

        introducePostRepository.delete(introducePost);
    }
}
