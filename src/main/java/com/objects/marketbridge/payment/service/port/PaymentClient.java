package com.objects.marketbridge.payment.service.port;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.payment.service.dto.PaymentDto;
import com.objects.marketbridge.payment.service.dto.RefundDto;

public interface PaymentClient {
    RefundDto refund(String tid, Integer cancelAmount);

    PaymentDto payment(Order order, String pgToken);
}
