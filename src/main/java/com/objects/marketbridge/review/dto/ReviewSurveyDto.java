package com.objects.marketbridge.review.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewSurveyDto {

    private Long reviewSurveyCategoryId;
    private String reviewSurveyCategoryName;
    private String content;

    @Builder
    public ReviewSurveyDto(Long reviewSurveyCategoryId, String reviewSurveyCategoryName, String content) {
        this.reviewSurveyCategoryId = reviewSurveyCategoryId;
        this.reviewSurveyCategoryName = reviewSurveyCategoryName;
        this.content = content;
    }
}
