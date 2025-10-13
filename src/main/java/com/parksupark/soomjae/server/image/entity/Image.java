package com.parksupark.soomjae.server.image.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Image extends BaseEntity {

    @Column(nullable = false)
    protected String imageUrl;

}