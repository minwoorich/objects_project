package com.objects.marketbridge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewModifiableValuesDto {

    private Integer rating; //별점, 1~5
    private List<ReviewSurveyDto> reviewSurveyDtoList = new ArrayList<>();
    private String content;
    private List<String> reviewImgUrls = new ArrayList<>();
    private String summary;

    @Builder
    public ReviewModifiableValuesDto(Integer rating, List<ReviewSurveyDto> reviewSurveyDtoList,
                                     String content, List<String> reviewImgUrls, String summary) {
        this.rating = rating;
        this.reviewSurveyDtoList = reviewSurveyDtoList;
        this.content = content;
        this.reviewImgUrls = reviewImgUrls;
        this.summary = summary;
    }
}
