package com.parksupark.soomjae.server.community.post.memberpost.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostGridProjection;
import com.parksupark.soomjae.server.community.post.memberpost.dto.SaveMemberPostRequest;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostIdResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostDetailResponse;
import com.parksupark.soomjae.server.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberPostService {

    MemberPostIdResponse createMemberPost(SaveMemberPostRequest request, Member member);

    MemberPostDetailResponse readMemberPost(Long memberPostId,
        UsernamePasswordUserDetails userDetails);

    Page<MemberPostGridProjection> readGridMemberPosts(Long memberId, Pageable pageable);

    MemberPostIdResponse updateMemberPost(SaveMemberPostRequest request, Long memberPostId, Member member);

    void deleteMemberPost(Long memberPostId, Member member);

}
