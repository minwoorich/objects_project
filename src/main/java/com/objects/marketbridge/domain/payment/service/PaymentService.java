package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
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
    private final OrderRepository orderRepository;

    public void create(TossPaymentsResponse tossPaymentsResponse) {

        // 1. Payment 엔티티 생성
        Payment payment = createPayment(tossPaymentsResponse);

        // 2. 연관관계 매핑
        ProdOrder order = orderRepository.findByOrderNo(tossPaymentsResponse.getOrderId());
        payment.linkProdOrder(order);

        // 3. 영속성 저장
        paymentRepository.save(payment);
    }

    private Payment createPayment(TossPaymentsResponse tossPaymentsResponse) {

        String orderNo = tossPaymentsResponse.getOrderId();
        String paymentType = tossPaymentsResponse.getPaymentType();
        String paymentMethod = tossPaymentsResponse.getPaymentMethod();
        String paymentKey = tossPaymentsResponse.getPaymentKey();
        String paymentStatus = tossPaymentsResponse.getPaymentStatus();
        String refundStatus = tossPaymentsResponse.getRefundStatus();
        PaymentCancel paymentCancel = tossPaymentsResponse.getCancels().get(0);
        Card card = tossPaymentsResponse.getCard();
        VirtualAccount virtualAccount = tossPaymentsResponse.getVirtualAccount();
        Transfer transfer = tossPaymentsResponse.getTransfer();

        return Payment.create(orderNo, paymentType, paymentMethod, paymentKey, paymentStatus, refundStatus,  card, virtualAccount, transfer);
    }

}
