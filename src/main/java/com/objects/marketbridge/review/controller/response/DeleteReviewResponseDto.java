package com.objects.marketbridge.review.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DeleteReviewResponseDto {

    Long reviewId;

    @Builder
    public DeleteReviewResponseDto(Long reviewId) {
        this.reviewId = reviewId;
    }
}