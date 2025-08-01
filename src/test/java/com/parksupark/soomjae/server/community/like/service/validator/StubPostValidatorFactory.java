package com.parksupark.soomjae.server.community.like.service.validator;

import com.parksupark.soomjae.server.community.validator.PostValidator;
import com.parksupark.soomjae.server.community.validator.PostValidatorFactory;
import java.util.HashMap;
import java.util.Map;

public class StubPostValidatorFactory implements PostValidatorFactory {

    private final Map<String, PostValidator> validatorMap = new HashMap<>();

    public StubPostValidatorFactory(Map<String, PostValidator> validatorMap) {
        this.validatorMap.putAll(validatorMap);
    }

    @Override
    public PostValidator getValidator(String postType) {
        return validatorMap.get(postType);
    }
}
