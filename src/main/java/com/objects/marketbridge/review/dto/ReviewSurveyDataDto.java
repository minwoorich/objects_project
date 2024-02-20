package com.objects.marketbridge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewSurveyDataDto {

    private String reviewSurveyCategoryData;
    private String writtenOrSelectedSurveyContentData;

    @Builder
    public ReviewSurveyDataDto(String reviewSurveyCategoryData, String writtenOrSelectedSurveyContentData) {
        this.reviewSurveyCategoryData = reviewSurveyCategoryData;
        this.writtenOrSelectedSurveyContentData = writtenOrSelectedSurveyContentData;
    }
}
