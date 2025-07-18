package com.parksupark.soomjae.server.community.validator;

import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.communitypost.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommunityPostValidator implements PostValidator {

    private final CommunityPostRepository communityPostRepository;

    @Override
    public String getPostType() {
        return PostConstant.COMMUNITY_POST_TYPE;
    }

    @Override
    public boolean isValid(Long postId) {
        return communityPostRepository.existsById(postId);
    }
}
