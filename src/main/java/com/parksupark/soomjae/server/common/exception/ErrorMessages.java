package com.parksupark.soomjae.server.common.exception;

public class ErrorMessages {

    public static final String COMMUNITY_POST_NOT_FOUND = "게시글을 찾을 수 없습니다.";
    public static final String MEETING_POST_NOT_FOUND = "모임 게시글을 찾을 수 없습니다.";
    public static final String INVALID_POST_TYPE_EXCEPTION_MESSAGE = "잘못된 요청입니다.";
    public static final String NOT_PARTICIPANT_OF_POST = "이 모임에 참여 중이지 않습니다.";
    public static final String MEETING_PARTICIPANTS_FULL_EXCEPTION_MESSAGE = "참여 인원이 모두 찼습니다.";
    public static final String ALREADY_PARTICIPATE_IN_POST = "이미 참여 중인 모임입니다.";
    public static final String INVALID_POST_ID_EXCEPTION_MESSAGE = "잘못된 요청입니다.";
    public static final String LIKE_NOT_FOUND_EXCEPTION_MESSAGE = "좋아요 정보를 찾을 수 없습니다.";
    public static final String ALREADY_LIKED_EXCEPTION_MESSAGE = "이미 좋아요를 누른 게시글입니다.";
    public static final String MEMBER_NOT_FOUND_EXCEPTION_MESSAGE = "회원 정보를 찾을 수 없습니다.";
    public static final String DUPLICATE_EMAIL_EXCEPTION_MESSAGE = "이미 사용 중인 이메일입니다.";
    public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE = "엔티티 레벨 unique 검증 실패";
    public static final String COMMENT_OWNER_MISMATCH_EXCEPTION_MESSAGE = "본인의 댓글만 삭제할 수 있습니다.";
    public static final String VALIDATION_FAILED_MESSAGE = "요청 바디 값 검증에 실패했습니다.";

    public static final String CLOSED_MEETING = "이미 모집종료된 모임입니다";

    public static final String REFRESH_TOKEN_NOT_FOUND_FROM_COOKIE_MESSAGE =
        "Refresh Token을 쿠키에서 찾을 수 없습니다.";
    public static final String REFRESH_TOKEN_VALIDATION_FAILED_MESSAGE =
        "Refresh Token 검증에 실패했습니다.";

    public static final String OAUTH2_EMAIL_NOT_VERIFIED_MESSAGE = "이메일이 인증되지 않았습니다.";
    public static final String OAUTH2_PROVIDER_NOT_SUPPORT_MESSAGE = "지원하지 않는 OAuth Provider: ";
    
    private ErrorMessages() {
    }
}
