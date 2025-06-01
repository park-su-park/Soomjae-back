package com.parksupark.soomjae.server.community.like.service.validator;

import com.parksupark.soomjae.server.common.exception.ErrorMessages;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostTypeException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PostValidatorFactory {

    private final Map<String, PostValidator> validatorMap;

    public PostValidatorFactory(List<PostValidator> validators) {
        this.validatorMap = validators.stream()
            .collect(Collectors.toMap(PostValidator::getPostType, v -> v));
    }

    public PostValidator getValidator(String postType) {
        return Optional.ofNullable(validatorMap.get(postType))
            .orElseThrow(() -> new InvalidPostTypeException(
                ErrorMessages.INVALID_POST_TYPE_EXCEPTION_MESSAGE + postType));
    }
}
