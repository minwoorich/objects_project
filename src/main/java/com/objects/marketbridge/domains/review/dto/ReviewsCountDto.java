package com.objects.marketbridge.domains.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewsCountDto {

    private Long count;

    @Builder
    public ReviewsCountDto(Long count) {
        this.count = count;
    }
}
