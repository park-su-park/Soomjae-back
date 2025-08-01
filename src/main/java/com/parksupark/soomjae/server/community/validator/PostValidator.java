package com.parksupark.soomjae.server.community.validator;

public interface PostValidator {

    boolean isValid(Long postId);

    String getPostType();
}
