package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.order.controller.response.TossPaymentsResponse;
import com.objects.marketbridge.domain.order.entity.Order;
import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CreatePaymentServiceTest {

    @Autowired OrderRepository orderRepository;
    @Autowired OrderDetailRepository orderDetailRepository;
    @Autowired ProductRepository productRepository;
    @Autowired CreatePaymentService createPaymentService;
    @Autowired PaymentRepository paymentRepository;

    @BeforeEach
    void init() {

        Order order = Order.builder()
                .orderNo("aaaa-aaaa-aaaa")
                .totalPrice(10000L)
                .orderName("홍길동")
                .build();
        orderRepository.save(order);

        OrderDetail orderDetail1 = OrderDetail.builder()
                .orderNo("aaaa-aaaa-aaaa")
                .build();

        OrderDetail orderDetail2 = OrderDetail.builder()
                .orderNo("aaaa-aaaa-aaaa")
                .build();
        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);

        for (OrderDetail orderDetail : orderDetails) {
            order.addOrderDetail(orderDetail);
        }

        orderDetailRepository.saveAll(orderDetails);

    }

    @DisplayName("Payment 엔티티를 생성해야한다.")
    @Test
    void createPayment(){

        //given
        Order order = orderRepository.findByOrderNo("aaaa-aaaa-aaaa");
        Card card = getCard("1234-5678", BankCode.HANA.getCode());
        TossPaymentsResponse tossPaymentsResponse =
                getTossPaymentResponse(order.getOrderNo(), "페이먼트키", card);


        //when
        createPaymentService.create(tossPaymentsResponse);

        //then
        Payment payment = paymentRepository.findByOrderNo(order.getOrderNo());

        assertThat(payment.getOrderNo()).isEqualTo("aaaa-aaaa-aaaa");
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

    @DisplayName("Payment 와 Order 가 연관관계 매핑이 되어 있어야한다.")
    @Test
    void mappingPaymentWithOrder(){
        //given
        Order order = orderRepository.findByOrderNo("aaaa-aaaa-aaaa");
        Card card = getCard("1234-5678", BankCode.HANA.getCode());
        TossPaymentsResponse tossPaymentsResponse =
                getTossPaymentResponse(order.getOrderNo(), "페이먼트키", card);


        //when
        createPaymentService.create(tossPaymentsResponse);

        //then
        Payment payment = paymentRepository.findByOrderNo("aaaa-aaaa-aaaa");
        assertThat(payment.getOrder()).isEqualTo(order);
    }
    
    @DisplayName("OrderDetail 에 paymentKey를 할당해줘야한다.")
    @Test
    void changePaymentKey(){

        //given
        Order order = orderRepository.findByOrderNo("aaaa-aaaa-aaaa");
        List<OrderDetail> orderDetails = order.getOrderDetails();

        Card card = getCard("1234-5678", BankCode.HANA.getCode());
        TossPaymentsResponse tossPaymentsResponse =
                getTossPaymentResponse(order.getOrderNo(), "페이먼트키", card);


        //when
        createPaymentService.create(tossPaymentsResponse);
        
        //then
        assertThat(orderDetails).hasSize(2);
        for (OrderDetail orderDetail : orderDetails) {
            assertThat(orderDetail.getPaymentKey()).isEqualTo(tossPaymentsResponse.getPaymentKey());
        }
    }
}