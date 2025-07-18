package com.parksupark.soomjae.server.community.validator;

public interface PostValidatorFactory {

    PostValidator getValidator(String postType);

}
