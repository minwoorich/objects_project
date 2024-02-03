package com.objects.marketbridge.review.controller.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadReviewResponseDto {

    private Long memberId;
    private Long productId;
    private Integer rating; //별점, 1~5
    private String content; //글자수 제한?

    @Builder
    public ReadReviewResponseDto(Long memberId, Long productId, Integer rating, String content) {
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }
}
