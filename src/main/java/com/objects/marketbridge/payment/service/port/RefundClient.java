package com.objects.marketbridge.payment.service.port;

import com.objects.marketbridge.payment.service.dto.RefundDto;

public interface RefundClient {
    RefundDto refund(String tid, Integer cancelAmount);
}
