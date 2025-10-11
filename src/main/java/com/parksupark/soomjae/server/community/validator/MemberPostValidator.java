package com.parksupark.soomjae.server.community.validator;

import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.post.memberpost.repository.MemberPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberPostValidator implements PostValidator{

    private final MemberPostRepository memberPostRepository;

    @Override
    public String getPostType() {
        return PostConstant.MEMBER_POST_TYPE;
    }

    @Override
    public boolean isValid(Long postId) {
        return memberPostRepository.existsById(postId);
    }
}
