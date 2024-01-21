package com.objects.marketbridge.domain.order.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefundInfoResponse {
    private Integer deliveryFee;
    private Integer refundFee;
    private Long discountPrice;
    private Long totalPrice;

    @Builder
    private RefundInfoResponse(Integer deliveryFee, Integer refundFee, Long discountPrice, Long totalPrice) {
        this.deliveryFee = deliveryFee;
        this.refundFee = refundFee;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }
}
