package com.objects.marketbridge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewSingleReadDto {

    private Long reviewId;
    private Long memberId;
    private Long productId;
    private Integer rating; //별점, 1~5
    private List<ReviewSurveyDataDto> reviewSurveyDataDtoList = new ArrayList<>();
    private String content;
    private List<String> reviewImgUrls = new ArrayList<>();

    @Builder

    public ReviewSingleReadDto(Long reviewId, Long memberId, Long productId, Integer rating,
                               List<ReviewSurveyDataDto> reviewSurveyDataDtoList,
                               String content, List<String> reviewImgUrls) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.reviewSurveyDataDtoList = reviewSurveyDataDtoList;
        this.content = content;
        this.reviewImgUrls = reviewImgUrls;
    }
}
