package com.parksupark.soomjae.server.community.like.service.validator;

import com.parksupark.soomjae.server.community.common.constant.PostConstant;

public class AlwaysValidCommunityPostValidatorStub implements PostValidator {

    @Override
    public boolean isValid(Long postId) {
        return true;
    }

    @Override
    public String getPostType() {
        return PostConstant.COMMUNITY_POST_TYPE;
    }
}
