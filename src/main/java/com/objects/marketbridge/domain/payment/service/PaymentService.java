package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.payment.domain.*;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void create(TossPaymentsResponse tossPaymentsResponse) {
        String paymentType = tossPaymentsResponse.getPaymentType();
        String paymentMethod = tossPaymentsResponse.getPaymentMethod();
        String paymentKey = tossPaymentsResponse.getPaymentKey();
        String paymentStatus = tossPaymentsResponse.getPaymentStatus();
        String refundStatus = tossPaymentsResponse.getRefundStatus();
        PaymentCancel paymentCancel = tossPaymentsResponse.getCancels().get(0);
        Card card = tossPaymentsResponse.getCard();
        VirtualAccount virtualAccount = tossPaymentsResponse.getVirtualAccount();
        Transfer transfer = tossPaymentsResponse.getTransfer();

        Payment.create(paymentType, paymentMethod, paymentKey, paymentStatus, refundStatus, paymentCancel, card, virtualAccount, transfer);
    }

}
