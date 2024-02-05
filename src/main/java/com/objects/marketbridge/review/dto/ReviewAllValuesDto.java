package com.objects.marketbridge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewAllValuesDto {

    private Long reviewId;
    private Long memberId;
    private Long productId;
    private Long orderDetailId;
    private List<String> reviewImgUrls = new ArrayList<>();
    private Integer rating; //별점, 1~5
    private String content;

    @Builder
    public ReviewAllValuesDto(Long reviewId, Long memberId, Long productId,
                              Long orderDetailId, List<String> reviewImgUrls,
                              Integer rating, String content) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.productId = productId;
        this.orderDetailId = orderDetailId;
        this.reviewImgUrls = reviewImgUrls;
        this.rating = rating;
        this.content = content;
    }
}
