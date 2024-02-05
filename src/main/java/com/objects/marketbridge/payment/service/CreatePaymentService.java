package com.objects.marketbridge.payment.service;

import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.controller.dto.CompleteOrderHttp;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.port.PaymentRepository;
import com.objects.marketbridge.seller.service.port.SellerAccountRepository;
import com.objects.marketbridge.seller.service.port.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.objects.marketbridge.order.domain.StatusCodeType.PAYMENT_COMPLETED;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final SellerRepository sellerRepository;
    private final SellerAccountRepository sellerAccountRepository;

    @Transactional
    public CompleteOrderHttp.Response create(KakaoPayApproveResponse response) {

        // 1. Payment 엔티티 생성
        Payment payment = createPayment(response);
        paymentRepository.save(payment);

        // 2. Order - Payment 연관관계 매핑
        Order order = orderQueryRepository.findByOrderNoWithOrderDetailsAndProduct(response.getPartnerOrderId());
        order.linkPayment(payment);

        // 3. orderDetail 의 statusCode 업데이트
        payment.changeStatusCode(PAYMENT_COMPLETED.getCode());

        // TODO : payment 대신 order?
        return CompleteOrderHttp.Response.of(payment);
    }

    private Payment createPayment(KakaoPayApproveResponse response) {

        String orderNo = response.getPartnerOrderId();
        String paymentMethod = response.getPaymentMethodType();
        String tid = response.getTid();
        CardInfo cardInfo = response.getCardInfo();
        Amount amount = response.getAmount();
        LocalDateTime approvedAt = response.getApprovedAt();

        return Payment.create(orderNo, paymentMethod, tid, cardInfo, amount, approvedAt);
    }
}
