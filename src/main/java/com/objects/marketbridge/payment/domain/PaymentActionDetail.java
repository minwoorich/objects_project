package com.objects.marketbridge.payment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentActionDetail {
    private String aid;
    private String approvedAt;
    private Long amount;
    private Long discountAmount;
    private String paymentActionType;
    private String payload;

    @Builder
    public PaymentActionDetail(String aid, String approvedAt, Long amount, Long discountAmount, String paymentActionType, String payload) {
        this.aid = aid;
        this.approvedAt = approvedAt;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.paymentActionType = paymentActionType;
        this.payload = payload;
    }
}
