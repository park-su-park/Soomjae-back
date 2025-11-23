package com.parksupark.soomjae.server.community.post.introductionpost.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.introductionpost.dto.UpdateIntroductionPostRequest;

public interface IntroductionPostService {

    PostResponse readIntroductionPostById(Long postId);

    PostResponse readIntroductionPostByMemberId(Long memberId);

    PostResponse updateIntroductionPost(UpdateIntroductionPostRequest request,
        UsernamePasswordUserDetails userDetails);

    void deleteIntroductionPostById(Long postId, UsernamePasswordUserDetails userDetails);

    void deleteIntroductionPostByMemberId(Long memberId, UsernamePasswordUserDetails userDetails);


}
