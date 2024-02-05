package com.objects.marketbridge.review.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateReviewResponseDto {

    private Long reviewId;
//    private Long orderDetailId;
//    private String orderDetailStatusCode;
    private Long memberId;
    private Long productId;
    private Integer rating; //별점, 1~5
    private String content; //글자수 제한?

    @Builder
    public UpdateReviewResponseDto(Long reviewId, Long memberId, Long productId, Integer rating, String content) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }
}
