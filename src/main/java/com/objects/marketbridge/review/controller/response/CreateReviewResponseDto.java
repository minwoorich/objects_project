package com.objects.marketbridge.review.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateReviewResponseDto {

    Long reviewId;

    public CreateReviewResponseDto(Long reviewId) {
        this.reviewId = reviewId;
    }
}
