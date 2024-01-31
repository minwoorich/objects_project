package com.objects.marketbridge.order.service;

import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import org.springframework.stereotype.Component;

public interface RefundService {
    RefundDto refund(String tid, Integer cancelAmount);
}
