package com.parksupark.soomjae.server.common.exception;

public class ErrorMessages {
    public static final String COMMUNITY_POST_NOT_FOUND = "해당 Id를 가진 커뮤니티 게시글이 존재하지 않습니다.";
    public static final String INVALID_POST_TYPE_EXCEPTION_MESSAGE = "잘못된 postType 입니다: ";
    public static final String INVALID_POST_ID_EXCEPTION_MESSAGE = "잘못된 postId 입니다: ";
    public static final String LIKE_NOT_FOUND_EXCEPTION_MESSAGE = "좋아요를 찾을 수 없습니다.";
    public static final String ALREADY_LIKED_EXCEPTION_MESSAGE = "이미 좋아요를 누른 게시글입니다.";

    private ErrorMessages() {
    }
}
