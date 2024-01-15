package com.objects.marketbridge.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TossParamsDto {
    private final String paymenrKey;
    private final String orderNo;
    private final Long totalPrice;

    @Builder
    public TossParamsDto(String paymenrKey, String orderNo, Long totalPrice) {
        this.paymenrKey = paymenrKey;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
    }
}
