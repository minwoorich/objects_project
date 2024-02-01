package com.objects.marketbridge.order.service;

import com.objects.marketbridge.payment.service.dto.RefundDto;

public interface RefundService {
    RefundDto refund(String tid, Integer cancelAmount);
}
