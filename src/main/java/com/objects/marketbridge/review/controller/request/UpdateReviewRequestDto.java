package com.objects.marketbridge.review.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateReviewRequestDto {

    @NotNull
    private Long reviewId;
    private Long orderDetailId;
    private String orderDetailStatusCode;
    private Long memberId;
    private Long productId;
    private Integer rating; //별점, 1~5
    private String content; //글자수 제한?

    @Builder
    public UpdateReviewRequestDto(Long reviewId, Long orderDetailId, String orderDetailStatusCode, Long memberId, Long productId, Integer rating, String content) {
        this.reviewId = reviewId;
        this.orderDetailId = orderDetailId;
        this.orderDetailStatusCode = orderDetailStatusCode;
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }
}
