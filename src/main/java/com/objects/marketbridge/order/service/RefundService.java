package com.objects.marketbridge.order.service;

import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.dto.RefundDto;
import org.springframework.stereotype.Component;

@Component
public class RefundService {
    public RefundDto refund(Payment payment, String cancelReason, Long realPrice) {
        return null;
    }
}
