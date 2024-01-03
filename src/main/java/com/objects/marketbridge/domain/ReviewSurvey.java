package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewSurvey extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_survey_id")
    private Long id;

    // TODO
    private Long reviewId;
    // TODO
    private Long surveyCategoryId;

    @Builder
    private ReviewSurvey(Long reviewId, Long surveyCategoryId) {
        this.reviewId = reviewId;
        this.surveyCategoryId = surveyCategoryId;
    }
}
