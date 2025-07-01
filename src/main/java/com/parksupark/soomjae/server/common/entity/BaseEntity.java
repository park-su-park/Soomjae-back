package com.parksupark.soomjae.server.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private Instant createdTime;

    @LastModifiedDate
    private Instant modifiedTime;

    private Instant deletedTime;

    public void markDeleted() {
        this.deletedTime = Instant.now();
    }
    // getter 생략 가능 (필요시 추가)
}

