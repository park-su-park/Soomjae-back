package com.parksupark.soomjae.server.member.dto;

import com.parksupark.soomjae.server.member.Role;
import java.time.Instant;

public interface MemberBasicInfo {

    Long getId();

    String getEmail();

    String getNickname();

    Role getRole();

    Instant getCreatedTime();

    Instant getModifiedTime();
}
