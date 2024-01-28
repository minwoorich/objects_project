package com.objects.marketbridge.order.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CancelRefundInfoResponse {
    private Long deliveryFee;
    private Long refundFee;
    private Long discountPrice;
    private Long totalPrice;

    @Builder
    private CancelRefundInfoResponse(Long deliveryFee, Long refundFee, Long discountPrice, Long totalPrice) {
        this.deliveryFee = deliveryFee;
        this.refundFee = refundFee;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }
}
