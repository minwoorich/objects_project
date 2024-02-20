package com.objects.marketbridge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateReviewDto {

    private Long productId;
    private Integer rating; //별점, 1~5
    private List<ReviewSurveyDto> reviewSurveys = new ArrayList<>();
    private String content;
    private List<ReviewImageDto> reviewImgUrls = new ArrayList<>();
    private String summary;

    @Builder
    public CreateReviewDto(Long productId,
                           Integer rating,
                           List<ReviewSurveyDto> reviewSurveys,
                           String content,
                           List<ReviewImageDto> reviewImgUrls,
                           String summary) {
        this.productId = productId;
        this.rating = rating;
        this.reviewSurveys = reviewSurveys;
        this.content = content;
        this.reviewImgUrls = reviewImgUrls;
        this.summary = summary;
    }
}
