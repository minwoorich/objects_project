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
    private String content;
    private List<String> reviewImgUrls = new ArrayList<>();

    @Builder
    public ReviewModifiableValuesDto(Integer rating, String content, List<String> reviewImgUrls) {
        this.rating = rating;
        this.content = content;
        this.reviewImgUrls = reviewImgUrls;
    }
}
