package com.objects.marketbridge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateReviewDto {

    //private Long memberId; //@AuthMemberId로 대체
    private Long productId;
    private Long orderDetailId;
    private Integer rating; //별점, 1~5
    private String content;
    private List<String> reviewImgUrls = new ArrayList<>();

    @Builder
    public CreateReviewDto(Long orderDetailId,
                     Long productId, Integer rating, String content,
                     List<String> reviewImgUrls) {

        this.orderDetailId = orderDetailId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
        this.reviewImgUrls = reviewImgUrls;
    }
}
