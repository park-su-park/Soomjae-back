package com.parksupark.soomjae.server.community.like.service.validator;

import com.parksupark.soomjae.server.community.common.constant.PostConstant;

public class AlwaysInvalidCommunityPostValidatorStub implements PostValidator {

    @Override
    public boolean isValid(Long postId) {
        return false;
    }

    @Override
    public String getPostType() {
        return PostConstant.COMMUNITY_POST_TYPE;
    }
}
