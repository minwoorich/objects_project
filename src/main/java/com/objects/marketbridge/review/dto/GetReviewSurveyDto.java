package com.objects.marketbridge.review.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReviewSurveyDto {
    private Long reviewSurveyId;
    private Long reviewSurveyCategoryId;
    private String reviewSurveyCategoryName;
    private String content;

    @Builder
    public GetReviewSurveyDto(Long reviewSurveyId, Long reviewSurveyCategoryId, String reviewSurveyCategoryName, String content) {
        this.reviewSurveyId = reviewSurveyId;
        this.reviewSurveyCategoryId = reviewSurveyCategoryId;
        this.reviewSurveyCategoryName = reviewSurveyCategoryName;
        this.content = content;
    }
}
