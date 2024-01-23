package com.objects.marketbridge.domain.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateOrderDetailDto {
    private Long productId;
    private Long usedCouponId;
    private Long quantity;
    private Long unitOrderPrice;

    @Builder
    public CreateOrderDetailDto(Long productId, Long usedCouponId, Long quantity, Long unitOrderPrice) {
        this.productId = productId;
        this.usedCouponId = usedCouponId;
        this.quantity = quantity;
        this.unitOrderPrice = unitOrderPrice;
    }
}
