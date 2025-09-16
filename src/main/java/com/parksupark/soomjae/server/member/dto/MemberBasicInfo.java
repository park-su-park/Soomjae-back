package com.parksupark.soomjae.server.member.dto;

import com.parksupark.soomjae.server.member.Role;
import java.time.Instant;

/**
 * 단순 Member 데이터 응답에 사용될 조회 성능 향상용 Projection 클래스
 *
 * <p>(Member는 칼럼이 적어 성능 향상이 미미하긴함)</p>
 */
public interface MemberBasicInfo {

    Long getId();

    String getEmail();

    String getNickname();

    Role getRole();

    Instant getCreatedTime();

    Instant getModifiedTime();
}
