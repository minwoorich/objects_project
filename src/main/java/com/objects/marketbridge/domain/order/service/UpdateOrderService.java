package com.objects.marketbridge.domain.order.service;

import com.objects.marketbridge.domain.coupon.repository.CouponRepository;
import com.objects.marketbridge.domain.order.controller.response.CreateOrderResponse;
import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.order.entity.StatusCode;
import com.objects.marketbridge.domain.order.entity.StatusCodeType;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.domain.Card;
import com.objects.marketbridge.domain.payment.domain.Payment;
import com.objects.marketbridge.domain.payment.domain.Transfer;
import com.objects.marketbridge.domain.payment.domain.VirtualAccount;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UpdateOrderService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;

    @Transactional
    public TossPaymentsResponse update(TossPaymentsResponse tossPaymentsResponse) {

        // 1. Payment 엔티티 생성
        Payment payment = createPayment(tossPaymentsResponse);

        // 2. Order - Payment 연관관계 매핑
        Order order = orderRepository.findByOrderNo(tossPaymentsResponse.getOrderId());
        payment.linkOrder(order);

        // 3. orderDetail 에 paymentKey, statusCode 업데이트
        List<OrderDetail> orderDetails = order.getOrderDetails();
        orderDetails.forEach(o -> {
            o.changePaymentKey(tossPaymentsResponse.getPaymentKey());
            o.changeStatusCode(StatusCodeType.PAYMENT_COMPLETED.getCode());
        });

        // 4. 영속성 저장
        paymentRepository.save(payment);

        // TODO : 쿠폰, 재고 업데이트 로직도 추가해야함

        return tossPaymentsResponse;
    }

    private Payment createPayment(TossPaymentsResponse tossPaymentsResponse) {

        String orderNo = tossPaymentsResponse.getOrderId();
        String paymentType = tossPaymentsResponse.getPaymentType();
        String paymentMethod = tossPaymentsResponse.getPaymentMethod();
        String paymentKey = tossPaymentsResponse.getPaymentKey();
        String paymentStatus = tossPaymentsResponse.getPaymentStatus();
        String refundStatus = tossPaymentsResponse.getRefundStatus();
        Card card = tossPaymentsResponse.getCard();
        VirtualAccount virtualAccount = tossPaymentsResponse.getVirtualAccount();
        Transfer transfer = tossPaymentsResponse.getTransfer();

        return Payment.create(orderNo, paymentType, paymentMethod, paymentKey, paymentStatus, refundStatus,  card, virtualAccount, transfer);
    }

}
