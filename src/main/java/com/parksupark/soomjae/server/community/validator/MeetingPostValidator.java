package com.parksupark.soomjae.server.community.validator;

import com.parksupark.soomjae.server.community.common.constant.PostConstant;
import com.parksupark.soomjae.server.community.post.meetingpost.repository.MeetingPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingPostValidator implements PostValidator {

    private final MeetingPostRepository meetingPostRepository;

    @Override
    public String getPostType() {
        return PostConstant.MEETING_POST_TYPE;
    }

    @Override
    public boolean isValid(Long postId) {
        return meetingPostRepository.existsById(postId);
    }
}
