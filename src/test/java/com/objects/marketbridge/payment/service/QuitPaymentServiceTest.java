package com.objects.marketbridge.payment.service;

import com.objects.marketbridge.member.domain.Address;
import com.objects.marketbridge.member.domain.AddressValue;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.order.service.port.OrderDtoRepository;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.port.PaymentRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.order.domain.StatusCodeType.PAYMENT_COMPLETED;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
class QuitPaymentServiceTest {

    @Autowired OrderCommendRepository orderCommendRepository;
    @Autowired OrderDtoRepository orderDtoRepository;
    @Autowired OrderQueryRepository orderQueryRepository;
    @Autowired OrderDetailQueryRepository orderDetailQueryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ProductRepository productRepository;
    @Autowired QuitPaymentService quitPaymentService;
    @Autowired PaymentRepository paymentRepository;

    @BeforeEach
    void init() {
        Address address = createAddress(createAddressValue("01011112222", "홍길동", "서울", "세종대로","11111", "민들레아파트110동3222호", "우리집"),true);
        Member member = Member.create(MembershipType.BASIC.getText(), "email.com", "1234", "홍길동", "01011112222", true, true);
        member.addAddress(address);
        memberRepository.save(member);

        Product product1 = Product.create(null, true, "상품1", 1000L, false, 100L, "썸네일1", 0L, "1번");
        Product product2 = Product.create(null, true, "상품2", 2000L, false, 100L, "썸네일2", 0L, "2번");
        Product product3 = Product.create(null, true, "상품3", 3000L, false, 100L, "썸네일3", 0L, "3번");
        Product product4 = Product.create(null, true, "상품4", 4000L, false, 100L, "썸네일4", 0L, "4번");
        productRepository.saveAll(List.of(product1, product2, product3, product4));

        OrderDetail orderDetail1 = OrderDetail.create("tid1", null, product1, "orderNo1", null, 1000L, 1L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail2 = OrderDetail.create("tid1", null, product2, "orderNo1", null, 1000L, 1L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail3 = OrderDetail.create("tid1", null, product3, "orderNo1", null, 1000L, 1L, null, PAYMENT_COMPLETED.getCode());

        OrderDetail orderDetail4= OrderDetail.create("tid2", null, product1, "orderNo2", null, 1000L, 2L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail5 = OrderDetail.create("tid2", null, product2, "orderNo2", null, 1000L, 2L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail6 = OrderDetail.create("tid2", null, product4, "orderNo2", null, 1000L, 2L, null, PAYMENT_COMPLETED.getCode());

        OrderDetail orderDetail7 = OrderDetail.create("tid3", null, product1, "orderNo3", null, 1000L, 3L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail8 = OrderDetail.create("tid3", null, product3, "orderNo3", null, 1000L, 3L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail9 = OrderDetail.create("tid3", null, product4, "orderNo3", null, 1000L, 3L, null, PAYMENT_COMPLETED.getCode());

        OrderDetail orderDetail10 = OrderDetail.create("tid4", null, product2, "orderNo4", null, 1000L, 4L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail11 = OrderDetail.create("tid4", null, product3, "orderNo4", null, 1000L, 4L, null, PAYMENT_COMPLETED.getCode());
        OrderDetail orderDetail12 = OrderDetail.create("tid4", null, product4, "orderNo4", null, 1000L, 4L, null, PAYMENT_COMPLETED.getCode());

        Order order1 = createOrder(member, address, "상품1 외 2건", "orderNo1", 3000L, "tid1", List.of(orderDetail1, orderDetail2, orderDetail3), null);
        Order order2 = createOrder(member, address, "상품1 외 2건", "orderNo2", 6000L, "tid2", List.of(orderDetail4, orderDetail5, orderDetail6), null);
        Order order3 = createOrder(member, address, "상품1 외 2건", "orderNo3", 9000L, "tid3", List.of(orderDetail7, orderDetail8, orderDetail9), null);
        Order order4 = createOrder(member, address, "상품2 외 2건", "orderNo4", 12000L, "tid4", List.of(orderDetail10, orderDetail11, orderDetail12), null);

        orderCommendRepository.saveAll(List.of(order1, order2, order3, order4));
    }
    private Address createAddress(AddressValue addressValue, Boolean isDefault) {
        return Address.create(addressValue, isDefault);
    }

    private AddressValue createAddressValue(String phoneNo, String name, String city, String street, String zipcode, String detail, String alias) {
        return AddressValue.create(phoneNo, name, city, street, zipcode, detail, alias);
    }

    private Order createOrder(Member member, Address address, String orderName, String orderNo, Long totalPrice, String tid, List<OrderDetail> orderDetails, Payment payment) {
        Order order = Order.create(member, address, orderName, orderNo, totalPrice, tid);

        // order <-> orderDetail 연관관계
        orderDetails.forEach(order::addOrderDetail);

        return order;
    }

    @DisplayName("주문을 soft_delete하면 payment, order_detail들도 모두 soft_delete 된다")
    @Test
    void cancel(){
        //given
        Order order = orderQueryRepository.findByOrderNo("orderNo1");
        List<OrderDetail> orderDetails = order.getOrderDetails();
        Long od1Id = orderDetails.get(0).getId();
        Long od2Id = orderDetails.get(1).getId();
        Long od3Id = orderDetails.get(2).getId();

        //when
        quitPaymentService.cancel(order.getOrderNo());

        //then
        assertThatThrownBy(() -> orderQueryRepository.findByOrderNo(order.getOrderNo())).isInstanceOf(JpaObjectRetrievalFailureException.class);
        assertThatThrownBy(() -> orderDetailQueryRepository.findById(od1Id)).isInstanceOf(JpaObjectRetrievalFailureException.class);
        assertThatThrownBy(() -> orderDetailQueryRepository.findById(od2Id)).isInstanceOf(JpaObjectRetrievalFailureException.class);
        assertThatThrownBy(() -> orderDetailQueryRepository.findById(od3Id)).isInstanceOf(JpaObjectRetrievalFailureException.class);
    }
}