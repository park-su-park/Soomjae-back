package com.parksupark.soomjae.server.community.post.memberpost.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.memberpost.dto.SaveMemberPostRequest;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostIdResponse;
import com.parksupark.soomjae.server.community.post.memberpost.dto.MemberPostDetailResponse;
import com.parksupark.soomjae.server.member.entity.Member;

public interface MemberPostService {

    MemberPostIdResponse createMemberPost(SaveMemberPostRequest request, Member member);

    MemberPostDetailResponse readMemberPost(Long memberPostId,
        UsernamePasswordUserDetails userDetails);

    MemberPostIdResponse updateMemberPost(SaveMemberPostRequest request, Long memberPostId, Member member);

    void deleteMemberPost(Long memberPostId, Member member);

}
