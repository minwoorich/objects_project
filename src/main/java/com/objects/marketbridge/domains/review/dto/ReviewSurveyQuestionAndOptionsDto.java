package com.objects.marketbridge.domains.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewSurveyQuestionAndOptionsDto {

    private String reviewSurveyQuestion;
    private List<String> reviewSurveyOptionList;

    @Builder
    public ReviewSurveyQuestionAndOptionsDto(String reviewSurveyQuestion, List<String> reviewSurveyOptionList) {
        this.reviewSurveyQuestion = reviewSurveyQuestion;
        this.reviewSurveyOptionList = reviewSurveyOptionList;
    }
}
