package com.objects.marketbridge.mock;

import com.objects.marketbridge.order.service.RefundService;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import lombok.Builder;

import java.time.LocalDateTime;

public class FakeRefundService implements RefundService {

    private TestDateTimeHolder dateTimeHolder;

    @Builder
    private FakeRefundService(TestDateTimeHolder dateTimeHolder) {
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
