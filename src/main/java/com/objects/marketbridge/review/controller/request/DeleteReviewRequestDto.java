package com.objects.marketbridge.review.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteReviewRequestDto {

    @NotNull
    private Long reviewId;
    private Long memberId;
    private Long productId;

    @Builder
    public DeleteReviewRequestDto(Long reviewId, Long memberId, Long productId) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.productId = productId;
    }
}
