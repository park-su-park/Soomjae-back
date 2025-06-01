package com.parksupark.soomjae.server.community.like.service.validator;

public interface PostValidatorFactory {

    PostValidator getValidator(String postType);

}
