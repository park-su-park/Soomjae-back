package com.parksupark.soomjae.server.community.review.entity;

import com.parksupark.soomjae.server.common.entity.BaseEntity;
import com.parksupark.soomjae.server.community.participation.entity.Participation;
import com.parksupark.soomjae.server.community.review.dto.UpdateReviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", unique = true)
    private Participation participation;

    @Column(precision = 2, scale = 1)
    private BigDecimal star;

    @Lob
    private String content;

    private Review(Participation participation, BigDecimal star, String content) {
        this.participation = participation;
        this.star = star;
        this.content = content;
    }

    public static Review create(Participation participation, BigDecimal star, String content) {
        return new Review(participation, star, content);
    }

    public void updateReview(UpdateReviewRequest request) {
        if (request.getStar() != null) {
            this.star = request.getStar();
        }
        if (request.getContent() != null) {
            this.content = request.getContent();
        }
    }
}
