package com.objects.marketbridge.mock;

import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.payment.service.port.RefundClient;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.Builder;

public class FakeRefundClient implements RefundClient {

    private DateTimeHolder dateTimeHolder;

    @Builder
    public FakeRefundClient(DateTimeHolder dateTimeHolder) {
        this.dateTimeHolder = dateTimeHolder;
    }

    @Override
    public RefundDto refund(String tid, Integer cancelAmount) {
        return RefundDto.builder()
                .totalRefundAmount(Long.valueOf(cancelAmount))
                .refundMethod("카드")
                .refundProcessedAt(dateTimeHolder.getTimeNow())
                .build();
    }
}
