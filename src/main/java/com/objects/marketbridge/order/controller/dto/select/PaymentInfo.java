package com.objects.marketbridge.order.controller.dto.select;

import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentInfo {
    private String paymentMethod;
    private String cardIssuerName;
    private Long totalAmount;
    private Long discountAmount;
    private Long deliveryFee;

    @Builder
    private PaymentInfo(String paymentMethod, String cardIssuerName, Long totalAmount, Long discountAmount, Long deliveryFee) {
        this.paymentMethod = paymentMethod;
        this.cardIssuerName = cardIssuerName;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.deliveryFee = deliveryFee;
    }

    public static PaymentInfo of(OrderDtio orderDtio) {
        return PaymentInfo.builder()
                .totalAmount(orderDtio.getTotalPrice())
                .discountAmount(orderDtio.getTotalDiscount())
                .paymentMethod(orderDtio.getPaymentMethod())
                .cardIssuerName(orderDtio.getCardIssuerName())
                .build();
    }
}
