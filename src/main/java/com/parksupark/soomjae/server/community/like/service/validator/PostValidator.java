package com.parksupark.soomjae.server.community.like.service.validator;

public interface PostValidator {

    boolean isValid(Long postId);

    String getPostType();
}
