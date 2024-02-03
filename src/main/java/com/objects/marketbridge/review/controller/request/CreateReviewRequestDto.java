package com.objects.marketbridge.review.controller.request;

import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReviewRequestDto {

    //리뷰는 상품결제후 배송완료/또는수취확인 된 상태에서만 작성가능하게 한다
    @NotNull
    private Long orderDetailId;
    @NotNull
    private String orderDetailStatusCode;
    @NotNull
    private Long memberId;
    @NotNull
    private Long productId;
    @NotNull
    private Integer rating; //별점, 1~5
    @NotNull 
    private String content; //글자수 제한?


    @Builder
    public CreateReviewRequestDto(Long orderDetailId, String orderDetailStatusCode, Long memberId, Long productId, Integer rating, String content) {
        this.orderDetailId = orderDetailId;
        this.orderDetailStatusCode = orderDetailStatusCode;
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }
}
