package com.parksupark.soomjae.server.community.like.service;

import com.parksupark.soomjae.server.auth.username.dto.UsernamePasswordUserDetails;
import com.parksupark.soomjae.server.community.like.dto.LikeStatusResponse;

public interface LikeService {

    LikeStatusResponse createLike(String postType, Long postId,
        UsernamePasswordUserDetails userDetails);

    LikeStatusResponse deleteLike(String postType, Long postId,
        UsernamePasswordUserDetails userDetails);

    LikeStatusResponse readLikeStatus(String postType, Long postId,
        UsernamePasswordUserDetails userDetails);
}
