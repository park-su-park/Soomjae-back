package com.parksupark.soomjae.server.community.like.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.common.exception.AlreadyLikedException;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostIdException;
import com.parksupark.soomjae.server.community.common.exception.LikeNotFoundException;
import com.parksupark.soomjae.server.community.like.dto.LikeStatusResponse;
import com.parksupark.soomjae.server.community.like.repository.InMemoryLikeRepository;
import com.parksupark.soomjae.server.community.like.repository.LikeRepository;
import com.parksupark.soomjae.server.community.like.service.validator.AlwaysInvalidCommunityPostValidatorStub;
import com.parksupark.soomjae.server.community.like.service.validator.AlwaysValidCommunityPostValidatorStub;
import com.parksupark.soomjae.server.community.like.service.validator.StubPostValidatorFactory;
import com.parksupark.soomjae.server.community.validator.PostValidator;
import com.parksupark.soomjae.server.community.validator.PostValidatorFactory;
import com.parksupark.soomjae.server.member.entity.Member;
import com.parksupark.soomjae.server.member.repository.JpaLikeInMemoryMemberRepository;
import com.parksupark.soomjae.server.member.repository.MemberRepository;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultLikeServiceTest {

    private LikeService likeService;
    private final LikeRepository likeRepository = new InMemoryLikeRepository();
    private final MemberRepository memberRepository = new JpaLikeInMemoryMemberRepository();
    private final String postType = "community";
    private final Long postId = 1L;
    private UsernamePasswordUserDetails userDetails;

    @BeforeEach
    void setUp() {
        ((InMemoryLikeRepository) likeRepository).clear();
        ((JpaLikeInMemoryMemberRepository) memberRepository).clear();

        Member member = memberRepository.save(Member.create("test@gmail.com", "test", "test"));
        this.userDetails = new UsernamePasswordUserDetails(member);

        Map<String, PostValidator> validatorMap = new HashMap<>();
        validatorMap.put("community", new AlwaysValidCommunityPostValidatorStub());
        PostValidatorFactory postValidatorFactory = new StubPostValidatorFactory(validatorMap);
        this.likeService = new DefaultLikeService(likeRepository, postValidatorFactory);
    }

    @Test
    void shouldThrowInvalidPostIdException_whenPostIdIsInvalid() {
        // given
        final Map<String, PostValidator> validatorMap = new HashMap<>();
        validatorMap.put("community", new AlwaysInvalidCommunityPostValidatorStub());

        final PostValidatorFactory postValidatorFactory = new StubPostValidatorFactory(
                validatorMap);
        likeService = new DefaultLikeService(likeRepository, postValidatorFactory);

        // when + then
        assertThrows(InvalidPostIdException.class,
                () -> likeService.createLike(postType, postId, userDetails));
    }

    @Test
    void shouldCreateLikeAndReturnResponse_whenUserHasNotLikedPostBefore() {
        // when
        LikeStatusResponse response = likeService.createLike(postType, postId, userDetails);

        // then
        assertNotNull(response);
        assertEquals(true, response.liked());
        assertEquals(1L, response.likeCount());
        assertEquals(1L, likeRepository.countByPostTypeAndPostId(postType, postId));
    }

    @Test
    void shouldThrowAlreadyLikedException_whenUserRequestCreateLikeInLikedPost() {
        // when
        likeService.createLike(postType, postId, userDetails);

        // then
        assertThrows(AlreadyLikedException.class,
                () -> likeService.createLike(postType, postId, userDetails));
    }

    @Test
    void shouldDeleteLikeAndReturnResponse_whenUserHasLikedPostBefore() {
        // given
        likeService.createLike(postType, postId, userDetails);

        // when
        LikeStatusResponse response = likeService.deleteLike(postType, postId, userDetails);

        // then
        assertNotNull(response);
        assertEquals(false, response.liked());
        assertEquals(0L, response.likeCount());
        assertEquals(0L, likeRepository.countByPostTypeAndPostId(postType, postId));
    }

    @Test
    void shouldThrowLikeNotFoundException_whenUserHasNotLikedPostBefore() {
        // when + then
        assertThrows(LikeNotFoundException.class,
                () -> likeService.deleteLike(postType, postId, userDetails));
    }

    @Test
    void shouldReturnResponse_whenUserRequestedLikeStatus() {
        // when
        LikeStatusResponse response = likeService.readLikeStatus(postType, postId, userDetails);

        // then
        assertEquals(false, response.liked());
        assertEquals(0L, response.likeCount());

        // when
        response = likeService.createLike(postType, postId, userDetails);

        // then
        assertEquals(true, response.liked());
        assertEquals(1L, response.likeCount());
    }


}