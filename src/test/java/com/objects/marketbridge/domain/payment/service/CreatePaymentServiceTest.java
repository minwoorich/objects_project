package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.payment.domain.BankCode;
import com.objects.marketbridge.domain.payment.domain.Card;
import com.objects.marketbridge.domain.payment.domain.Payment;
import com.objects.marketbridge.domain.payment.service.port.PaymentRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CreatePaymentServiceTest {

    @Autowired OrderRepository orderRepository;
    @Autowired ProductRepository productRepository;
    @Autowired CreatePaymentService createPaymentService;
    @Autowired PaymentRepository paymentRepository;
    @BeforeEach
    void init() {

        ProdOrder order = ProdOrder.builder()
                .orderNo("aaaa-aaaa-aaaa")
                .totalPrice(10000L)
                .orderName("홍길동")
                .build();

        orderRepository.save(order);
    }

    @DisplayName("Payment 엔티티를 생성해야한다.")
    @Test
    void createPayment(){
        //given
        ProdOrder order = orderRepository.findByOrderNo("aaaa-aaaa-aaaa");
        Card card = getCard("1234-5678", BankCode.HANA.getCode());
        TossPaymentsResponse tossPaymentsResponse =
                getTossPaymentResponse(order.getOrderNo(), "페이먼트키", card);

        //when
        createPaymentService.create(tossPaymentsResponse);

        //then
        Payment payment = paymentRepository.findByOrderNo(order.getOrderNo());

        assertThat(payment.getOrderNo()).isEqualTo(order.getOrderNo());
        assertThat(payment.getPaymentKey()).isEqualTo("페이먼트키");
        assertThat(payment.getCard()).isEqualTo(card);

    }

    private  TossPaymentsResponse getTossPaymentResponse(String orderNo, String paymentKey, Card card) {
        return TossPaymentsResponse.builder()
                .orderId(orderNo)
                .paymentKey(paymentKey)
                .card(card)
                .build();
    }
    private static Card getCard(String cardNo, String cardIssuerCode) {
        return Card.builder()
                .cardNo(cardNo)
                .cardIssuerCode(cardIssuerCode)
                .build();
    }
}