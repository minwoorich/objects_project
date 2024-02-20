package com.objects.marketbridge.review.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewSurvey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_survey_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_survey_category_id")
    private ReviewSurveyCategory reviewSurveyCategory;

    private String surveyCategory;

    private String content;

    private String summary;

    @Builder
    public ReviewSurvey(Review review, ReviewSurveyCategory reviewSurveyCategory,
                        String surveyCategory, String content, String summary) {
        this.review = review;
        this.reviewSurveyCategory = reviewSurveyCategory;
        this.surveyCategory = surveyCategory;
        this.content = content;
        this.summary = summary;
    }
}
