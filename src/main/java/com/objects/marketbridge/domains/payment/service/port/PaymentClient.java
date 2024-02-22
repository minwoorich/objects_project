package com.objects.marketbridge.domains.payment.service.port;

import com.objects.marketbridge.domains.payment.service.dto.PaymentDto;
import com.objects.marketbridge.domains.order.domain.Order;
import com.objects.marketbridge.domains.payment.service.dto.RefundDto;

public interface PaymentClient {
    RefundDto refund(String tid, Integer cancelAmount);

    PaymentDto payment(Order order, String pgToken);
}
