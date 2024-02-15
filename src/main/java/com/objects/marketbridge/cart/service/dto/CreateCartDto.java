package com.objects.marketbridge.cart.service.dto;

import com.objects.marketbridge.cart.domain.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCartDto {
    private String productNo;
    private Long memberId;
    private Long quantity;
    private Boolean isSubs;

    @Builder
    private CreateCartDto(String productNo, Long memberId, Long quantity, Boolean isSubs) {
        this.productNo = productNo;
        this.memberId = memberId;
        this.quantity = quantity;
        this.isSubs = isSubs;
    }

    public static CreateCartDto create(String productNo, Long memberId,Long quantity,  Boolean isSubs) {
        return CreateCartDto.builder()
                .productNo(productNo)
                .memberId(memberId)
                .quantity(quantity)
                .isSubs(isSubs)
                .build();
    }
}
