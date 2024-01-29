package com.objects.marketbridge.order.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CancelRefundInfoServiceDto {

    private Long deliveryFee;
    private Long refundFee;
    private Long discountPrice;
    private Long totalPrice;

    @Builder
    private CancelRefundInfoServiceDto(Long deliveryFee, Long refundFee, Long discountPrice, Long totalPrice) {
        this.deliveryFee = deliveryFee;
        this.refundFee = refundFee;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }
}
