package com.parksupark.soomjae.server.common.constant;

public class ValidationMessages {

    // 필수 값
    public static final String REQUIRED = "필수 항복입니다.";
    public static final String NOT_BLANK = "빈 값은 허용되지 않습니다.";
    public static final String NOT_NULL = "null 값은 허용되지 않습니다.";
    public static final String NOT_EMPTY = "비어있을 수 없습니다.";

    // 이메일
    public static final String EMAIL_INVALID_FORMAT = "올바른 이메일 형식이 아닙니다.";

    // 리뷰
    public static final String REVIEW_STAR_MIN_VALUE = "별점은 0.0 이상이어야 합니다.";
    public static final String REVIEW_STAR_MAX_VALUE = "별점은 5.0 이하여야 합니다.";

    private ValidationMessages() {

    }

}
