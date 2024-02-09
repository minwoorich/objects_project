package com.objects.marketbridge.order.controller.dto.select;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentInfo {
    private String paymentMethod;
    private Long totalAmount;
    private Long discountAmount;
    private Long deliveryFee;

    @Builder
    public PaymentInfo(String paymentMethod, Long totalAmount, Long discountAmount, Long deliveryFee) {
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.deliveryFee = deliveryFee;
    }
}
