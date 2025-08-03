package com.parksupark.soomjae.server.common.exception;

public class ErrorMessages {

    public static final String COMMUNITY_POST_NOT_FOUND = "해당 Id를 가진 커뮤니티 게시글이 존재하지 않습니다.";

    public static final String MEETING_POST_NOT_FOUND = "해당 Id를 가진 모임 게시글이 존재하지 않습니다.";
    public static final String INVALID_POST_TYPE_EXCEPTION_MESSAGE = "잘못된 postType 입니다: ";
    public static final String INVALID_POST_ID_EXCEPTION_MESSAGE = "잘못된 postId 입니다: ";
    public static final String LIKE_NOT_FOUND_EXCEPTION_MESSAGE = "좋아요를 찾을 수 없습니다.";
    public static final String ALREADY_LIKED_EXCEPTION_MESSAGE = "이미 좋아요를 누른 게시글입니다.";
    public static final String MEMBER_NOT_FOUND_EXCEPTION_MESSAGE = "사용자를 찾을 수 없습니다.";
    public static final String DUPLICATE_EMAIL_EXCEPTION_MESSAGE = "이미 사용중인 email 입니다.";
    public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE = "엔티티 레벨 unique 검증 실패";

    public static final String COMMENT_OWNER_MISMATCH_EXCEPTION_MESSAGE = "삭제하려는 댓글이 본인의 댓글이 아닙니다.";


    public static final String VALIDATION_FAILED_MESSAGE = "요청 바디 값 검증에 실패했습니다.";


    private ErrorMessages() {
    }
}
