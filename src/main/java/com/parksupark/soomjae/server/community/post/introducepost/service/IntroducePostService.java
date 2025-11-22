package com.parksupark.soomjae.server.community.post.introducepost.service;

import com.google.api.services.storage.Storage.Projects.HmacKeys.Create;
import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.post.common.dto.PostResponse;
import com.parksupark.soomjae.server.community.post.introducepost.dto.CreateIntroducePostRequest;
import com.parksupark.soomjae.server.community.post.introducepost.dto.UpdateIntroducePostRequest;

public interface IntroducePostService {

    Long createIntroducePost(CreateIntroducePostRequest request,
        UsernamePasswordUserDetails userDetails);

    PostResponse readIntroducePostById(Long postId);

    PostResponse readIntroducePostByMemberId(Long memberId);

    PostResponse updateIntroducePost(UpdateIntroducePostRequest request,
        UsernamePasswordUserDetails userDetails);

    void deleteIntroducePostById(Long postId, UsernamePasswordUserDetails userDetails);

    void deleteIntroducePostByMemberId(Long memberId, UsernamePasswordUserDetails userDetails);


}
